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
     * */
    public boolean isConclued(String auctionGoodsId,int customerId){
        //1、参拍人数>=最低参拍人数（拍品表）
        int ActualPeopleNum =  dealMapper.selectActualPeopleNum(auctionGoodsId);
        Map<String, Object> auctionMinParam = dealMapper.selectAuctionMinParam(auctionGoodsId);
        if(auctionMinParam!=null){
            int minPeopleNum = (int)auctionMinParam.get("min_number_people");
            if(ActualPeopleNum<minPeopleNum){
                return false;
            }
           //2、出价在成交区间内
            int firsrRankingUserId = dealMapper.selectRankFirstUserId(auctionGoodsId);
            BigDecimal  maxBid = dealMapper.selectMaxBid(auctionGoodsId,firsrRankingUserId);
            BigDecimal min_section  = (BigDecimal)auctionMinParam.get("min_section");
            if(maxBid.compareTo(min_section)<0){
                return false;
            }
            //3、两个利润公式
            //利润公式1：出价+总排豆>成本+利润
            List<DealConditionVo> dealConditionVo = dealMapper.selectDealInfoById(auctionGoodsId);
            BigDecimal profit=null;
            BigDecimal cost=null;
            BigDecimal beans_pond=null;
            if(dealConditionVo!=null){
                profit = dealConditionVo.get(0).getProfit();
                cost = dealConditionVo.get(0).getCost();
                beans_pond = dealConditionVo.get(0).getBeans_pond();
            }
            if(maxBid.add(beans_pond).compareTo(profit.add(cost))<0){
                return false;
            }
            //利润公式2  总排豆-返佣>利润。

        }else{
           return false;
        }
        //应该抛异常
        return false;
    }


   public List<DealConditionVo> getdealConditionInfo(String auctionGoodsId) {

       return dealMapper.selectDealInfoById(auctionGoodsId);

    }
}
