package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.DealMapper;
import com.example.auctionapp.service.IDealService;
import com.example.auctionapp.vo.DealConditionVo;
import com.example.auctionapp.vo.SecondWinRate;
import com.example.auctionapp.vo.WinRateVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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

    //自定义降序排序器
    public class DescComparator implements Comparator<BigDecimal> {
        public int compare(BigDecimal value1,BigDecimal value2){
            return value2.subtract(value1).intValue();
        }
    }
      //计算拍中几率接口
      //input：拍卖值列表 goodsValueList
      //outpur:拍中几率列表
      public List<BigDecimal> calWinRate(List<BigDecimal> goodsValueList){
          if(goodsValueList==null){
              return null;
          }
          //首先对拍卖值列表降序排列
          Collections.sort(goodsValueList,new DescComparator());
          BigDecimal highestGoodsValue = goodsValueList.get(0);
          //第一次划分区间
          BigDecimal intervalIncrementValue = highestGoodsValue.multiply(new BigDecimal(String.valueOf(0.2)));
          intervalIncrementValue.setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal doubleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(2))).setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal tripleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(3))).setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal quadrupleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(4))).setScale(2,BigDecimal.ROUND_HALF_UP);

          WinRateVo winRateVo1 = new WinRateVo(new BigDecimal(String.valueOf(0)) ,intervalIncrementValue,50.00,60.00,0);
          WinRateVo winRateVo2 = new WinRateVo(intervalIncrementValue ,doubleIntervalIncrementValue,60.00,70.00,0);
          WinRateVo winRateVo3 = new WinRateVo(doubleIntervalIncrementValue ,tripleIntervalIncrementValue,70.00,80.00,0);
          WinRateVo winRateVo4 = new WinRateVo(tripleIntervalIncrementValue ,quadrupleIntervalIncrementValue,80.00,90.00,0);
          WinRateVo winRateVo5 = new WinRateVo(quadrupleIntervalIncrementValue ,highestGoodsValue,90.00,100.00,0);
          List<WinRateVo> winRateVos = new ArrayList<>();
          winRateVos.add(winRateVo1);
          winRateVos.add(winRateVo2);
          winRateVos.add(winRateVo3);
          winRateVos.add(winRateVo4);
          winRateVos.add(winRateVo5);
          //统计各区间人数
          for(BigDecimal goodsValue : goodsValueList){
              if(goodsValue.compareTo(new BigDecimal(String.valueOf(0)))>0 && goodsValue.compareTo(intervalIncrementValue)<=0){
                  winRateVo1.setIntervalPeopleNum(winRateVo1.getIntervalPeopleNum()+1);
              }else if(goodsValue.compareTo(intervalIncrementValue)>0 && goodsValue.compareTo(doubleIntervalIncrementValue)<=0) {
                  winRateVo2.setIntervalPeopleNum(winRateVo2.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(doubleIntervalIncrementValue)>0 && goodsValue.compareTo(tripleIntervalIncrementValue)<=0) {
                  winRateVo3.setIntervalPeopleNum(winRateVo3.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(tripleIntervalIncrementValue)>0 && goodsValue.compareTo(quadrupleIntervalIncrementValue)<=0) {
                  winRateVo4.setIntervalPeopleNum(winRateVo4.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(quadrupleIntervalIncrementValue)>0 && goodsValue.compareTo(highestGoodsValue)<=0) {
                  winRateVo5.setIntervalPeopleNum(winRateVo5.getIntervalPeopleNum() + 1);
              }
          }
          //第二次划分区间
          for(WinRateVo winRateVo:winRateVos){
              double secondWinRateIncrement = ((winRateVo.getWinRateHighValue()-winRateVo.getWinRateLowValue())/winRateVo.getIntervalPeopleNum());
              BigDecimal bg = new BigDecimal(secondWinRateIncrement).setScale(2, BigDecimal.ROUND_HALF_UP);
              secondWinRateIncrement = bg.doubleValue();
              List<SecondWinRate> secondWinRateList = new ArrayList<>();
              for(int i=0;i<winRateVo.getIntervalPeopleNum();i++){
                  SecondWinRate secondWinRate = new SecondWinRate();
                  secondWinRate.setLowRate(winRateVo.getWinRateLowValue()+i*secondWinRateIncrement);
                  secondWinRate.setHighRate(winRateVo.getWinRateLowValue()+(i+1)*secondWinRateIncrement);
                  if (i==winRateVo.getIntervalPeopleNum()-1){
                      secondWinRate.setHighRate(winRateVo.getWinRateHighValue());
                  }
                  secondWinRateList.add(secondWinRate);
              }
              winRateVo.setSecondWinRateList(secondWinRateList);
          }
          List<BigDecimal> winRates = new ArrayList<>();
          //随机分配区间和拍中概率
          Random ran = new Random();
          for(WinRateVo winRateVo:winRateVos){
              //拍卖值高的 拍中几率也要高 所以就按顺序分配区间 每个区间生成一个随机数就行了 就是从小到大的 最后返回的时候再和输入的userId对应。。
              List<SecondWinRate> secondWinRateList = winRateVo.getSecondWinRateList();
              for(int i=0;i<winRateVo.getIntervalPeopleNum();i++){
                  SecondWinRate secondWinRate = secondWinRateList.get(i);
                  //生成[secondWinRate.getLowRate(),secondWinRate.getHighRate()]
                  double randomIndex = ran.nextDouble(secondWinRate.getHighRate());
                  randomIndexs[i] = randomIndex+1;

              }
          }
          //再将所有的数组拼起来。。
          BigDecimal intervalIncrementValue =
          return null;
      }


    public static void main(String[] args) {
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        BigDecimal bean1 = new BigDecimal("1.0");
        BigDecimal bean3 = new BigDecimal("1.5");

        BigDecimal bean2 = new BigDecimal("2.0");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        DealServiceImpl dsi = new DealServiceImpl();
        dsi.calWinRate(list);

//        Collections.sort(list,new MyComparator());
//        System.out.println(list.get(0).para);
//        System.out.println(list.get(1).para);
    }

}
