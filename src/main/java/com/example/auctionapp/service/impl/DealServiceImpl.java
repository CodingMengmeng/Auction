package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.DealMapper;
import com.example.auctionapp.service.IDealService;
import com.example.auctionapp.vo.DealConditionVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class DealServiceImpl implements IDealService {

    @Resource private DealMapper dealMapper;
    /*
     * 根据拍品id查询排中相关参数
     * */
    public Map<String, Object> getGoodsDealParamById(String auctionGoodsId){
        System.out.println("service:"+auctionGoodsId);
        return dealMapper.selectAuctionMinParam(auctionGoodsId);
    }
    /*
     * 是否拍中函数
     * @auctionGoodsId 拍品Id
     * 返回true/false
     * */
    public boolean isDealConclued(String auctionGoodsId) throws Exception{
        //1、参拍人数>=最低参拍人数（拍品表）
        int ActualPeopleNum =  dealMapper.selectActualPeopleNum(auctionGoodsId);
        Map<String, Object> auctionMinParam = dealMapper.selectAuctionMinParam(auctionGoodsId);
        if(auctionMinParam!=null){
            int minPeopleNum = (int)auctionMinParam.get("min_number_people");
            if(ActualPeopleNum<minPeopleNum){
                return false;
            }
           //2、出价在成交区间内
             //拍卖值排名最高的用户的出价(所有出价之和)>=最小成交价
            int firsrRankingUserId = dealMapper.selectRankFirstUserId(auctionGoodsId);
            BigDecimal  maxBid = dealMapper.selectMaxBid(auctionGoodsId,firsrRankingUserId);
            BigDecimal min_section  = (BigDecimal)auctionMinParam.get("min_section");
            if(maxBid.compareTo(min_section)<0){
                return false;
            }
            //3、两个利润公式
            //3.1利润公式1：出价+总排豆>=成本+利润
            List<DealConditionVo> dealConditionVo = dealMapper.selectDealInfoById(auctionGoodsId);
            BigDecimal profit=null;//利润
            BigDecimal cost=null;//成本
            BigDecimal beans_pond=null;//总拍豆
            if(dealConditionVo!=null){
                profit = dealConditionVo.get(0).getProfit();
                cost = dealConditionVo.get(0).getCost();
                beans_pond = dealConditionVo.get(0).getBeans_pond();
            }else{
                throw new Exception("该商品拍品表参数为空,商品id为"+auctionGoodsId);
            }
            if(maxBid.add(beans_pond).compareTo(profit.add(cost))<0){
                return false;
            }
            //3.2利润公式2  总排豆-返佣>=利润。
             //查询用户的佣金=支付的排豆*2
            BigDecimal totalPayedBeans = dealMapper.selectTotalPayedBeans(auctionGoodsId,firsrRankingUserId);
            if(beans_pond.subtract(totalPayedBeans.multiply(new BigDecimal(Double.valueOf("2")))).compareTo(profit)<0){
                    return false;
            }else{
                return true;
            }
        }else{
           throw new Exception("该商品最小成交参数为空,商品id为"+auctionGoodsId);
        }
    }


   public List<DealConditionVo> getdealConditionInfo(String auctionGoodsId) {

       return dealMapper.selectDealInfoById(auctionGoodsId);

    }
}
