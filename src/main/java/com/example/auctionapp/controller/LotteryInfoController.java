package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.ActivityInfo;
import com.example.auctionapp.entity.LotteryInfo;
import com.example.auctionapp.service.IActivityInfoService;
import com.example.auctionapp.service.ILotteryInfoService;
import com.example.auctionapp.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.ResponseDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽奖信息表 前端控制器
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Slf4j
@RestController
@RequestMapping("/lottery-info")
public class LotteryInfoController {

    @Autowired
    private IActivityInfoService activityInfoService;

    @Autowired
    private ILotteryInfoService lotteryInfoService;

    @Autowired
    private IRedisService redisService;


    /**
     * 分页查询用户的抽奖码
     * @param page
     * @param size
     * @param customerId
     * @param token
     * @param code
     * @return
     */
    @WebLog(value = "查询用户的抽奖码")
    @PostMapping("/getCustomerLotteryCodePage")
    public Result getCustomerLotteryCodePage(
                                @RequestHeader("userId") Integer customerId,
                                @RequestHeader("token") String token,
                                @RequestParam("page") Integer page,
                                @RequestParam("size") Integer size,
                                @RequestParam("code") String code) {

        String userToken= String.valueOf(redisService.get(ProjectConstant.AUCTION_LOGIN_TOKEN_KEY + customerId));
        log.info("## redis 获取当前用户={},userToken={}",customerId,userToken);
        if(!userToken.equals(token)){
            return Result.errorInfo("用户token不一致，请重新登录");
        }

        ActivityInfo activityInfo=activityInfoService.getByCode(code);
        if(activityInfo==null){
           return  Result.errorInfo("活动不存在");
        }

        LotteryInfo lotteryInfo =new LotteryInfo();
        lotteryInfo.setCustomerId(customerId);
        lotteryInfo.setActivityId(activityInfo.getId());
        Map map= lotteryInfoService.pageList(page,size,lotteryInfo);
        return  Result.success(map);
    }

}
