package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.ActivityInfo;
import com.example.auctionapp.entity.LotteryInfo;
import com.example.auctionapp.entity.LotteryResult;
import com.example.auctionapp.service.IActivityInfoService;
import com.example.auctionapp.service.ILotteryInfoService;
import com.example.auctionapp.service.ILotteryResultService;
import com.example.auctionapp.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 抽奖结果表 前端控制器
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Slf4j
@RestController
@RequestMapping("/lottery-result")
public class LotteryResultController {

    @Autowired
    private IActivityInfoService activityInfoService;


    @Autowired
    private ILotteryResultService lotteryResultService;

    @Autowired
    private IRedisService redisService;


    /**
     *  查找活动的中奖券
     * @param code
     * @return
     */
    @WebLog(value = "查找活动的中奖券")
    @PostMapping("/getLotteryResultByCode")
    public Result getLotteryResultByCode(@RequestHeader("userId") Integer customerId,
                                         @RequestHeader("token") String token,
                                         @RequestParam("code") String code) {

        ActivityInfo activityInfo=activityInfoService.getByCode(code);
        if(activityInfo==null) {
            return Result.errorInfo("活动不存在");
        }

        String userToken= String.valueOf(redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + customerId));
        log.info("## redis 获取当前用户={},userToken={}",customerId,userToken);
        if(!userToken.equals(token)){
            return Result.errorInfo("用户token不一致，请重新登录");
        }

        LotteryResult lotteryResult=new LotteryResult();
        lotteryResult.setActivityId(activityInfo.getId());
        List<LotteryResult> listData= lotteryResultService.getLotteryResultByCode(lotteryResult);
        return  Result.success(listData);
    }


    /**
     *  查找用户是否中奖
     * @param code
     * @return
     */
    @WebLog(value = "用户是否中奖")
    @PostMapping("/getLotteryResultByUser")
    public Result getLotteryResultByUser(@RequestHeader("userId") Integer customerId,
                                         @RequestHeader("token") String token,
                                         @RequestParam("code") String code) {

        ActivityInfo activityInfo=activityInfoService.getByCode(code);
        if(activityInfo==null) {
            return Result.errorInfo("活动不存在");
        }

        String userToken= String.valueOf(redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + customerId));
        log.info("## redis 获取当前用户={},userToken={}",customerId,userToken);
        if(!userToken.equals(token)){
            return Result.errorInfo("用户token不一致，请重新登录");
        }

        LotteryResult lotteryResult=new LotteryResult();
        lotteryResult.setActivityId(activityInfo.getId());
        lotteryResult.setCustomerId(customerId);

        QueryWrapper queryWrapper=new QueryWrapper(lotteryResult);
        int count= lotteryResultService.count(queryWrapper);

        return  Result.success(count);
    }
}
