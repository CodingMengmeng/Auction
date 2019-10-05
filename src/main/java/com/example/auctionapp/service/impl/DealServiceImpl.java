package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.DealMapper;
import com.example.auctionapp.service.IDealService;
import com.example.auctionapp.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    //按拍卖值降序排序器
    public class GoodsValueDescComparator implements Comparator<WinRateRequestVo> {
        public int compare(WinRateRequestVo winRateRequestVo1,WinRateRequestVo winRateRequestVo2){
            BigDecimal value1 = winRateRequestVo1.getGoodsValue();
            BigDecimal value2 = winRateRequestVo2.getGoodsValue();
            return value2.subtract(value1).intValue();
        }
    }
    //按拍卖值升序排序器
    public class GoodsValueAscComparator implements Comparator<WinRateRequestVo> {
        public int compare(WinRateRequestVo winRateRequestVo1,WinRateRequestVo winRateRequestVo2){
            BigDecimal value1 = winRateRequestVo1.getGoodsValue();
            BigDecimal value2 = winRateRequestVo2.getGoodsValue();
            return value1.subtract(value2).intValue();
        }
    }
      //计算拍中几率接口
      //input：拍卖值列表 goodsValueList
      //output:拍中几率列表
      public List<WinRateResponseVo> calWinRate(List<WinRateRequestVo> winRateRequestVoList){
          if(winRateRequestVoList==null || winRateRequestVoList.size()<=0){
              return null;
          }
          //首先对拍卖值列表降序排列
          Collections.sort(winRateRequestVoList,new GoodsValueDescComparator());
          BigDecimal highestGoodsValue = winRateRequestVoList.get(0).getGoodsValue();
          //第一次划分区间
          BigDecimal intervalIncrementValue = highestGoodsValue.multiply(new BigDecimal(String.valueOf(0.2)));
          intervalIncrementValue.setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal doubleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(2))).setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal tripleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(3))).setScale(2,BigDecimal.ROUND_HALF_UP);
          BigDecimal quadrupleIntervalIncrementValue = intervalIncrementValue.multiply(new BigDecimal(String.valueOf(4))).setScale(2,BigDecimal.ROUND_HALF_UP);

          FirstWinRateIntervalVo firstWinRateIntervalVo1 = new FirstWinRateIntervalVo(new BigDecimal(String.valueOf(0)) ,intervalIncrementValue,BigDecimal.valueOf(50.00) ,BigDecimal.valueOf(60.00),0);
          FirstWinRateIntervalVo firstWinRateIntervalVo2 = new FirstWinRateIntervalVo(intervalIncrementValue ,doubleIntervalIncrementValue,BigDecimal.valueOf(60.00) ,BigDecimal.valueOf(70.00),0);
          FirstWinRateIntervalVo firstWinRateIntervalVo3 = new FirstWinRateIntervalVo(doubleIntervalIncrementValue ,tripleIntervalIncrementValue,BigDecimal.valueOf(70.00) ,BigDecimal.valueOf(80.00),0);
          FirstWinRateIntervalVo firstWinRateIntervalVo4 = new FirstWinRateIntervalVo(tripleIntervalIncrementValue ,quadrupleIntervalIncrementValue,BigDecimal.valueOf(80.00) ,BigDecimal.valueOf(90.00),0);
          FirstWinRateIntervalVo firstWinRateIntervalVo5 = new FirstWinRateIntervalVo(quadrupleIntervalIncrementValue ,highestGoodsValue,BigDecimal.valueOf(90.00) ,BigDecimal.valueOf(100.00),0);
          List<FirstWinRateIntervalVo> firstWinRateIntervalVos = new ArrayList<>();
          firstWinRateIntervalVos.add(firstWinRateIntervalVo1);
          firstWinRateIntervalVos.add(firstWinRateIntervalVo2);
          firstWinRateIntervalVos.add(firstWinRateIntervalVo3);
          firstWinRateIntervalVos.add(firstWinRateIntervalVo4);
          firstWinRateIntervalVos.add(firstWinRateIntervalVo5);
          //统计各区间人数
          for(WinRateRequestVo winRateRequestVo : winRateRequestVoList){
              BigDecimal goodsValue = winRateRequestVo.getGoodsValue();
              if(goodsValue.compareTo(new BigDecimal(String.valueOf(0)))>0 && goodsValue.compareTo(intervalIncrementValue)<=0){
                  firstWinRateIntervalVo1.setIntervalPeopleNum(firstWinRateIntervalVo1.getIntervalPeopleNum()+1);
              }else if(goodsValue.compareTo(intervalIncrementValue)>0 && goodsValue.compareTo(doubleIntervalIncrementValue)<=0) {
                  firstWinRateIntervalVo2.setIntervalPeopleNum(firstWinRateIntervalVo2.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(doubleIntervalIncrementValue)>0 && goodsValue.compareTo(tripleIntervalIncrementValue)<=0) {
                  firstWinRateIntervalVo3.setIntervalPeopleNum(firstWinRateIntervalVo3.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(tripleIntervalIncrementValue)>0 && goodsValue.compareTo(quadrupleIntervalIncrementValue)<=0) {
                  firstWinRateIntervalVo4.setIntervalPeopleNum(firstWinRateIntervalVo4.getIntervalPeopleNum() + 1);
              }else if(goodsValue.compareTo(quadrupleIntervalIncrementValue)>0 && goodsValue.compareTo(highestGoodsValue)<=0) {
                  firstWinRateIntervalVo5.setIntervalPeopleNum(firstWinRateIntervalVo5.getIntervalPeopleNum() + 1);
              }
          }
          //第二次划分区间
          for(FirstWinRateIntervalVo firstWinRateIntervalVo : firstWinRateIntervalVos){
              BigDecimal secondWinRateIncrement = (firstWinRateIntervalVo.getWinRateHighValue().subtract(firstWinRateIntervalVo.getWinRateLowValue())).divide(BigDecimal.valueOf(firstWinRateIntervalVo.getIntervalPeopleNum()));
              secondWinRateIncrement.setScale(2,RoundingMode.HALF_UP);
              List<SecondWinRateIntervalVo> secondWinRateIntervalVoList = new ArrayList<>();
              for(int i = 0; i< firstWinRateIntervalVo.getIntervalPeopleNum(); i++){
                  SecondWinRateIntervalVo secondWinRateIntervalVo = new SecondWinRateIntervalVo();
                  secondWinRateIntervalVo.setLowRate(firstWinRateIntervalVo.getWinRateLowValue().add(secondWinRateIncrement.multiply(BigDecimal.valueOf(i))));
                  secondWinRateIntervalVo.setHighRate(firstWinRateIntervalVo.getWinRateLowValue().add(secondWinRateIncrement.multiply(BigDecimal.valueOf(i+1))));
                  //如果是最后一个区间，直接加精度会多一点，所以直接set整数
                  if (i== firstWinRateIntervalVo.getIntervalPeopleNum()-1){
                      secondWinRateIntervalVo.setHighRate(firstWinRateIntervalVo.getWinRateHighValue());
                  }
                  secondWinRateIntervalVoList.add(secondWinRateIntervalVo);
              }
              //一个第一次的区间对应1-多个二次划分区间
              firstWinRateIntervalVo.setSecondWinRateIntervalVoList(secondWinRateIntervalVoList);
          }
          List<BigDecimal> winRates = new ArrayList<>();
          //先从小到大生成随机的随机拍中概率
          Random random = new Random();
          for(FirstWinRateIntervalVo firstWinRateIntervalVo : firstWinRateIntervalVos){
              //拍卖值高的 拍中几率也要高 所以就按顺序分配区间 每个区间生成一个随机数就行了 就是从小到大的
              //最后返回的时候再和输入的userId对应。。想想输入输出的结构
              List<SecondWinRateIntervalVo> secondWinRateIntervalVoList = firstWinRateIntervalVo.getSecondWinRateIntervalVoList();
              for(int i = 0; i< firstWinRateIntervalVo.getIntervalPeopleNum(); i++){
                  SecondWinRateIntervalVo secondWinRateIntervalVo = secondWinRateIntervalVoList.get(i);
                  //生成[secondWinRateIntervalVo.getLowRate(),secondWinRateIntervalVo.getHighRate()]区间内的随机浮点数
                  Double randomMin = secondWinRateIntervalVo.getLowRate().doubleValue();
                  Double randomMax = secondWinRateIntervalVo.getHighRate().doubleValue();
                  double randomWinRate = randomMin+random.nextDouble()*(randomMax-randomMin);
                  BigDecimal randomWinRateBD = BigDecimal.valueOf(randomWinRate).setScale(2, RoundingMode.HALF_UP);
                  winRates.add(randomWinRateBD);

              }
          }
          //按拍卖值大小依次分配区间..其实就对输入的拍卖值升序排序，依次赋值拍中几率就行了
          //组装返回对象
          List<WinRateResponseVo> WinRateResponseList = new ArrayList<>();
          for(int i=0;i<winRateRequestVoList.size();i++){
              BigDecimal winRate = winRates.get(i);
              WinRateResponseVo winRateResponseVo = new WinRateResponseVo(winRateRequestVoList.get(i).getGoodsValue(),
                      winRateRequestVoList.get(i).getCustomerId(), winRate);
              WinRateResponseList.add(winRateResponseVo);
          }
          return WinRateResponseList;
      }


    public static void main(String[] args) {
        List<WinRateRequestVo> winRateRequestVoList = new ArrayList<>();
        WinRateRequestVo WinRateRequestVo1 = new WinRateRequestVo();
        WinRateRequestVo1.setCustomerId(1);
        WinRateRequestVo1.setGoodsValue(BigDecimal.valueOf(100));
        WinRateRequestVo WinRateRequestVo2 = new WinRateRequestVo();
        WinRateRequestVo1.setCustomerId(2);
        WinRateRequestVo1.setGoodsValue(BigDecimal.valueOf(101));
        WinRateRequestVo WinRateRequestVo3 = new WinRateRequestVo();
        WinRateRequestVo1.setCustomerId(3);
        WinRateRequestVo1.setGoodsValue(BigDecimal.valueOf(99));
        winRateRequestVoList.add(WinRateRequestVo1);
        winRateRequestVoList.add(WinRateRequestVo2);
        winRateRequestVoList.add(WinRateRequestVo3);
        DealServiceImpl dsi = new DealServiceImpl();
        System.out.println( dsi.calWinRate(winRateRequestVoList));

//        Collections.sort(list,new MyComparator());
//        System.out.println(list.get(0).para);
//        System.out.println(list.get(1).para);
    }

}
