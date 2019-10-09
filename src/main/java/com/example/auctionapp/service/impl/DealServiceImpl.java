package com.example.auctionapp.service.impl;

import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.dao.DealMapper;
import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.AuctionGoodsDealInfo;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.IDealService;
import com.example.auctionapp.util.DateTimeUtil;
import com.example.auctionapp.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DealServiceImpl implements IDealService {

    @Resource private DealMapper dealMapper;

    @Resource
    TransLogMapper transLogMapper;
    /*
     * 根据拍品id查询排中相关参数
     * */
    public Map<String, Object> getGoodsDealParamById(int auctionGoodsId){
        System.out.println("service:"+auctionGoodsId);
        return dealMapper.selectAuctionMinParam(auctionGoodsId);
    }
    /*
     * 是否拍中函数
     * @auctionGoodsId 拍品Id
     * 返回true/false
     * */
    public DealConcluedVo isDealConclued(int auctionGoodsId) throws Exception{
        DealConcluedVo dealConcluedVo = new DealConcluedVo();
        //1、参拍人数>=最低参拍人数（拍品表）
        int ActualPeopleNum =  dealMapper.selectActualPeopleNum(auctionGoodsId);
        Map<String, Object> auctionMinParam = dealMapper.selectAuctionMinParam(auctionGoodsId);
        if(auctionMinParam!=null){
            int minPeopleNum = (int)auctionMinParam.get("min_number_people");
            if(ActualPeopleNum<minPeopleNum){
                dealConcluedVo.setConclued(false);
                return dealConcluedVo;
            }
           //2、出价在成交区间内
             //拍卖值排名最高的用户的出价(所有出价之和)>=最小成交价
            int firsrRankingUserId = dealMapper.selectRankFirstUserId(auctionGoodsId);
            BigDecimal  maxBid = dealMapper.selectMaxBid(auctionGoodsId,firsrRankingUserId);
            BigDecimal min_section  = (BigDecimal)auctionMinParam.get("min_section");
            if(maxBid.compareTo(min_section)<0){
                dealConcluedVo.setConclued(false);
                return dealConcluedVo;
            }
            //3、两个利润公式
            //3.1利润公式1：出价+总排豆>=成本+利润
            AuctionGoodsDealInfo auctionGoodsDealInfo = dealMapper.selectDealInfoById(auctionGoodsId);
            BigDecimal profit=null;//利润
            BigDecimal cost=null;//成本
            BigDecimal beans_pond=null;//总拍豆
            if(auctionGoodsDealInfo!=null){
                profit = auctionGoodsDealInfo.getProfit();
                cost = auctionGoodsDealInfo.getCost();
                beans_pond = auctionGoodsDealInfo.getBeansPond();
            }else{
                throw new Exception("该商品拍品表参数为空,商品id为"+auctionGoodsId);
            }
            if(maxBid.add(beans_pond).compareTo(profit.add(cost))<0){
                dealConcluedVo.setConclued(false);
                return dealConcluedVo;
            }
            //3.2利润公式2  总排豆-返佣>=利润。
             //查询用户的佣金=支付的排豆*2
            BigDecimal totalPayedBeans = dealMapper.selectTotalPayedBeans(auctionGoodsId,firsrRankingUserId);
            if(beans_pond.subtract(totalPayedBeans.multiply(new BigDecimal(Double.valueOf("2")))).compareTo(profit)<0){
                dealConcluedVo.setConclued(false);
                return dealConcluedVo;
            }else{
                dealConcluedVo.setConcluedUserId(firsrRankingUserId);
                dealConcluedVo.setConclued(true);
                return dealConcluedVo;
            }
        }else{
           throw new Exception("该商品最小成交参数为空,商品id为"+auctionGoodsId);
        }
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
      //input：拍卖者id,拍卖值
      //output:拍卖者id，拍卖值，拍中几率
      //【拍卖值和几率都是左开右闭，保留两位小数】
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
              int intervalPeopelNum = firstWinRateIntervalVo.getIntervalPeopleNum();
              if(intervalPeopelNum==0){
                  //该区间人数为0的话，第二次不用划分区间
                  continue;
              }
              BigDecimal secondWinRateIncrement = (firstWinRateIntervalVo.getWinRateHighValue().subtract(firstWinRateIntervalVo.getWinRateLowValue())).divide(BigDecimal.valueOf(intervalPeopelNum),2,RoundingMode.HALF_UP);
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
          Collections.sort(winRateRequestVoList,new GoodsValueAscComparator());
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

    //代理返佣接口
    //输入：拍中者id,拍品编号
    //输出：执行成功为1，否则为0
    public int executeAgentCommision(int concluedCustomerId,int goodsId){
        //1、计算佣金
         //返佣基数-
        AuctionGoodsDealInfo auctionGoodsDealInfo = dealMapper.selectDealInfoById(goodsId);
        BigDecimal beansPond = auctionGoodsDealInfo.getBeansPond();
        //用户支付佣金
        BigDecimal totalPayedBeans = dealMapper.selectTotalPayedBeans(goodsId,concluedCustomerId);
        //返佣基数=总排豆当前值-用户返佣（用户支付拍豆乘2）-平台返佣（用户支付拍豆乘10%）
        BigDecimal commisionBaseAmount = beansPond.subtract(totalPayedBeans.multiply(BigDecimal.valueOf(2.1)));
         //查返佣系数
        //先查agent表的默认比例
        //用customer表和agent表关联
        BigDecimal commisionRate = dealMapper.selectAgentProportion(concluedCustomerId);
        //查不到的话再查固定比例
        //固定比例表的逻辑是，先算出佣金--用户支付的总拍豆，取佣金在start_price和end_price之间的比例
        if(commisionRate==null){
            commisionRate = dealMapper.selectProfitModulProportion(totalPayedBeans);
        }
        //【公式待修改】
        BigDecimal commisionAmount = commisionBaseAmount.multiply(commisionRate);
        //2、佣金入流水表(新insert一条记录，sign入1 type入4，入下order编号 拍品编号 发起者编号)
        TransLog transLog = new TransLog();
        transLog.setOrderNumber(ProjectConstant.SHARE_PROFIT + DateTimeUtil.getNowInSS() + concluedCustomerId);
        //商品ID goods_id
        transLog.setGoodsId(goodsId);
        //交易发起者 subject
        transLog.setSubject(concluedCustomerId);
        //交易状态 成功
        transLog.setStatus(1);
        //支付标记--收入
        transLog.setTransSign(1);
        //交易类型--返佣
        transLog.setTransType(8);
        //佣金
        transLog.setCommission(commisionAmount);
        transLogMapper.insert(transLog);
        //3、拍品表更新总拍豆--【待修改】
        beansPond = beansPond.subtract(commisionAmount);
        if(dealMapper.updateBeansPonds(beansPond,goodsId)>0){
            return 1;
        }
        else{
            return 0;
        }
    }

    //平台返佣接口
    //输入：拍品编号
    //输出：执行成功为1，否则为0
    public int executePlatformCommision(int concluedCustomerId,int goodsId){
       BigDecimal beansPond =  dealMapper.selectBeansPond(goodsId);
       BigDecimal totalPayedBeans = dealMapper.selectTotalPayedBeans(goodsId,concluedCustomerId);
       BigDecimal platformCommision = totalPayedBeans.multiply(BigDecimal.valueOf(0.1));
        // 流水表插入记录
        TransLog transLog = new TransLog();
        transLog.setOrderNumber(ProjectConstant.SHARE_PROFIT + DateTimeUtil.getNowInSS() + concluedCustomerId);
        //商品ID goods_id
        transLog.setGoodsId(goodsId);
        //交易发起者 subject
        transLog.setSubject(concluedCustomerId);
        //交易状态 成功
        transLog.setStatus(1);
        //支付标记--收入
        transLog.setTransSign(1);
        //交易类型--返佣
        transLog.setTransType(8);
        transLog.setCommission(platformCommision);
        if(transLogMapper.insert(transLog)<=0){
            return 0;
        }
        //总拍豆减去平台佣金
        if(dealMapper.updateBeansPonds(beansPond.subtract(platformCommision),goodsId)<=0){
            return 0;
        }
        else{
            return 1;
        }
    }

    //用户返佣接口
    //输入：拍中者id,拍品编号
    //输出：执行成功为1，否则为0
    public int executeCustomerCommision(int concluedCustomerId,int goodsId) {
        //拍品表新加一个冻结的排豆的字段，入用户佣金。然后总拍豆减去冻结的排豆
        BigDecimal totalPayedBeans = dealMapper.selectTotalPayedBeans(goodsId,concluedCustomerId);
        BigDecimal customerCommision = totalPayedBeans.multiply(BigDecimal.valueOf(2));
        BigDecimal beansPond =  dealMapper.selectBeansPond(goodsId);
        if(dealMapper.updateCustomerCommision(beansPond.subtract(customerCommision),customerCommision,goodsId)>0){
            return 1;
        }else{
            return 0;
        }
    }

    public BigDecimal getWinRateByCustomerId(int customerId,int goodsId){
        return dealMapper.getWinRateByCustomerId(customerId,goodsId);
    }

        public static void main(String[] args) {
        List<WinRateRequestVo> winRateRequestVoList = new ArrayList<>();
        BufferedReader bfr=null;
        try {
            bfr = new BufferedReader(new FileReader("D:\\研究工作\\外包接口\\拍中几率测试数据.txt"));
            String str = null;
            int customerId = 1;
            while((str=bfr.readLine())!=null){
                 BigDecimal goodsValue = BigDecimal.valueOf(Double.valueOf(str));
                 WinRateRequestVo WinRateRequestVo1 = new WinRateRequestVo(goodsValue,customerId);
                 winRateRequestVoList.add(WinRateRequestVo1);
                 customerId++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        WinRateRequestVo WinRateRequestVo1 = new WinRateRequestVo();
//        WinRateRequestVo1.setCustomerId(1);
//        WinRateRequestVo1.setGoodsValue(BigDecimal.valueOf(200));
//        WinRateRequestVo WinRateRequestVo2 = new WinRateRequestVo();
//        WinRateRequestVo2.setCustomerId(2);
//        WinRateRequestVo2.setGoodsValue(BigDecimal.valueOf(101));
//        WinRateRequestVo WinRateRequestVo3 = new WinRateRequestVo();
//        WinRateRequestVo3.setCustomerId(3);
//        WinRateRequestVo3.setGoodsValue(BigDecimal.valueOf(99));
//        WinRateRequestVo WinRateRequestVo4= new WinRateRequestVo();
//        WinRateRequestVo4.setCustomerId(4);
//        WinRateRequestVo4.setGoodsValue(BigDecimal.valueOf(80));
//        WinRateRequestVo WinRateRequestVo5 = new WinRateRequestVo();
//        WinRateRequestVo5.setCustomerId(5);
//        WinRateRequestVo5.setGoodsValue(BigDecimal.valueOf(85));
//        WinRateRequestVo WinRateRequestVo6 = new WinRateRequestVo();
//        WinRateRequestVo6.setCustomerId(6);
//        WinRateRequestVo6.setGoodsValue(BigDecimal.valueOf(75));
//        winRateRequestVoList.add(WinRateRequestVo1);
//        winRateRequestVoList.add(WinRateRequestVo2);
//        winRateRequestVoList.add(WinRateRequestVo3);
//        winRateRequestVoList.add(WinRateRequestVo4);
//        winRateRequestVoList.add(WinRateRequestVo5);
//        winRateRequestVoList.add(WinRateRequestVo6);
        DealServiceImpl dsi = new DealServiceImpl();
        List<WinRateResponseVo> list = dsi.calWinRate(winRateRequestVoList);
        for(WinRateResponseVo vo:list){
            System.out.println(vo.getCustomerId()+","+vo.getGoodsValue()+","+vo.getWinRate() );
        }
    }

}
