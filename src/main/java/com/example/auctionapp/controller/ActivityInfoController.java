package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.ActivityInfo;
import com.example.auctionapp.entity.CouponManage;
import com.example.auctionapp.entity.Customer;
import com.example.auctionapp.service.*;
import com.example.auctionapp.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动信息表 前端控制器
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Slf4j
@RestController
@RequestMapping("/activity-info")
public class ActivityInfoController {


    @Autowired
    private IActivityInfoService activityInfoService;

    @Autowired
    private ICouponManageService couponManageService;


    @Autowired
    private ILotteryInfoService lotteryInfoService;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private ICustomerService customerService;

    @WebLog(value = "获取活动编号")
    @GetMapping("/getCode")
    public Result getCode(@RequestParam("type") Integer type) {
        ActivityInfo activityInfo=activityInfoService.getActivityInfoByTypeAndCurTime(type);
        if(activityInfo ==null){
            //本次活动已结束，请等待下次活动开始
            activityInfo = activityInfoService.getLastActivityInfoByType(type,LocalDateTime.now());
            if(activityInfo ==null){
                return  Result.errorInfo("本次活动已结束，请等待下次活动开始").setCode(10001);
            }else{
                return  Result.errorInfo("本次活动已结束，本次活动编号"+activityInfo.getActivityCode()).setCode(10002).setData(activityInfo.getActivityCode());
            }
        }else{
            return Result.success(activityInfo.getActivityCode());
        }
    }


    /**
     * 查询活动
     *
     * @param code 活动信息主键
     * @return
     * @author 朱秋友
     */
    @WebLog(value = "查询活动信息")
    @GetMapping("/getByCode")
    public Result getByCode(@RequestParam("code") String code) {
        ActivityInfo activityInfo=activityInfoService.getByCode(code);
        if(activityInfo==null){
            return  Result.errorInfo("活动不存在");
        }
        LocalDateTime nowTime=LocalDateTime.now();
        //如果活动已经开启，判断当前时间是否在活动的时间范围内
        if(ActivityInfo.activityFlag1.equals(activityInfo.getActivityFlag())){
            //如果当前时间不在开启时间之后 或者 当前时间不在失效时间之前 则认为当前活动没开启获取理解已经失效
            if(!nowTime.isAfter(activityInfo.getStartTime()) || ! nowTime.isBefore(activityInfo.getExpireTime())){
                activityInfo.setActivityFlag(ActivityInfo.activityFlag0);
            }
        }
        activityInfo.setCurrDateTime(nowTime);
        log.info("# activity-info getByCode after -->{}", activityInfo.toString());
        return Result.success(activityInfo);
    }

    /**
     * 查询用户的优惠券数量
     * @param customerId
     * @param couponsType
     * @return
     */
    @WebLog(value = "查询用户的优惠券数量")
    @PostMapping("getUserCouponCount")
    public Result getUserCouponCount(@RequestHeader("userId") int customerId,@RequestParam("couponsType") int couponsType){
        int count= couponManageService.getCouponManageByUserAndType(customerId,couponsType,null);
        return Result.success(count);
    }

    /**
     * 查询用户的拍卖获获取优惠券数量
     * @param customerId
     * @param couponsType
     * @return
     */
    @WebLog(value = "查询用户的拍卖获获取优惠券数量")
    @PostMapping("getUserCouponCountForAuction")
    public Result getUserCouponCountForAuction(@RequestHeader("userId") int customerId,@RequestParam("couponsType") int couponsType){
        int count= couponManageService.getCouponManageByUserAndType(customerId,couponsType,Constants.CouponSendType.sendType4.getType());
        return Result.success(count);
    }



    /**
     * 查询用户的优惠券 轮播信息
     * @param couponsType
     * @return
     */
    @WebLog(value = "用户获取的优惠券轮播信息")
    @PostMapping("getCustomerCouponTop")
    public Result getCustomerCouponTop(@RequestParam("couponsType") int couponsType){
        List<CouponManage> listManages= couponManageService.getCouponManageForTop(couponsType);
        if(listManages !=null && listManages.size() >0) {
            for (int i=0;i<listManages.size();i++) {
                int sendType=listManages.get(i).getSendType();
                listManages.get(i).setSendTypeStr(Constants.CouponSendType.findByType(sendType).getDesc());
            }
        }
        return Result.success(listManages);
    }


    /**
     * 营销活动 用户开宝箱
     * @param customerId
     * @param token
     * @param code 营销活动编号
     * @return
     */
    @WebLog(value = "用户开宝箱")
    @PostMapping("doLotteryByCode")
    public Result doLotteryByCode(@RequestHeader("userId") Integer customerId,
                                  @RequestHeader("token") String token,
                                  @RequestParam("code") String code,
                                  @RequestParam(name="couponsType",required = false) Integer couponsType){
        String userToken= String.valueOf(redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + customerId));

        log.info("## redis 获取当前用户={},userToken={}",customerId,userToken);
       if(!userToken.equals(token)){
           return Result.errorInfo("用户token不一致，请重新登录");
       }
        //开启抽奖活动
        Result result=lotteryInfoService.doLottery(customerId,code);

        //查询活动剩下优惠券
        if(couponsType ==null){
            couponsType= ICouponsService.couponsType4;
        }
        int count= couponManageService.getCouponManageByUserAndType(customerId,couponsType,null);
        result.setTotal(Long.valueOf(count));
        return result;
    }

    /**
     * 当前用户排行榜
     * @param customerId
     * @param token
     * @param code
     * @return
     */
    @WebLog(value = "当前用户排行榜")
    @PostMapping("getUserRank")
    public Result getUserRank(@RequestHeader("userId") Integer customerId,
                              @RequestHeader("token") String token,
                              @RequestParam("code") String code) {

        //查询用户信息
        Map userMap=new HashMap();
        Customer customer=customerService.getById(customerId); //获取用户信息

        long rank= redisService.getActivityReverseRank(code,customerId);
        long score =redisService.getActivityReverseScore(code,customerId).longValue();
        if(userMap !=null){
            userMap.put("id", customer.getId());
            userMap.put("name",customer.getName());
            userMap.put("headPortrait",customer.getHeadPortrait());
            userMap.put("rank",rank);
            userMap.put("score",score);
        }
        return  Result.success(userMap);
    }


    /**
     * 获取当前活动排行信息 Top10
    * @param code
     * @return
     */
    @WebLog(value = "当前活动排行榜信息")
    @GetMapping("getActivityInfoRank")
    public Result getActivityInfoRank( @RequestParam("code") String code) {
        //获取排行信息
        List<ImmutablePair<String, Double>> rankList=redisService.getActivityRevRangeWithScores(code,0,10);

        ImmutablePair p1=null;
        List<Integer> ids=new ArrayList<>();
        Customer customer=null;

        List<Map<String,Object>> activityRanks=new ArrayList<>(); //活动排行列表
        Map<String,Object> customerMap=null;
        for(int i=0;i<rankList.size();i++){
            p1=rankList.get(i);
            ids.add(new Integer(p1.getLeft().toString()));
            customer=customerService.getById(new Integer(p1.getLeft().toString())); //获取用户信息

            //封装用户信息和排名 以及抽奖券数量
            if(customer !=null) {
                customerMap = new HashMap<>();
                customerMap.put("id", customer.getId());
                customerMap.put("name",customer.getName());
                customerMap.put("headPortrait",customer.getHeadPortrait());
                customerMap.put("score",p1.getRight());
                customerMap.put("rank",(i+1));
                activityRanks.add(customerMap);
            }
        }
        return  Result.success(activityRanks);
    }


}
