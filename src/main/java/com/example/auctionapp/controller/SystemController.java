package com.example.auctionapp.controller;

import com.example.auctionapp.configurer.ApplicationStartup;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.AppConfig;
import com.example.auctionapp.service.IRedisService;
import com.example.auctionapp.service.IAppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zll on 2018/6/13.
 *
 * app 基础模块
 */
@RestController
@RequestMapping("/sys")
@Api(value = "系统配置接口",description = "系统配置接口")
public class SystemController {

    @Resource
    private IAppConfigService iAppConfigService;
    @Resource
    private IRedisService iRedisService;

    @Autowired
    private ApplicationStartup applicationStartup;


    /**
     * 获得对应平台的版本信息
     * @param appConfig 通过verion进行查询，测试用
     * @return 返回对应平台（Android、iOS）信息
     */
    @PostMapping("/getAppConfig")
    @ApiOperation(value = "获得对应平台的版本信息",notes = "获得对应平台的版本信息",produces = "application/json")
    public Result getAppConfig(@RequestBody AppConfig appConfig) {
        appConfig = iAppConfigService.getAppConfig(appConfig);
        return ResultGenerator.genSuccessResult(appConfig);
    }

//    /**
////     * 更新用户最后一次登录时间
////     */
////    @PostMapping("/updateLastLoginTime")
////    @ApiOperation(value = "更新用户最后一次登录时间",notes = "更新用户最后一次登录时间",produces = "application/json")
////    public Result updateLastLoginTime(@RequestBody Map<String,String> maps) {
////        String userId = maps.get("userId");
////        if(StringUtils.isEmpty(userId)){
////            return ResultGenerator.genSuccessMEssageResult("更新失败！userId为null");
////        }
////        int size = iAppConfigService.updateLastLoginTimeById(userId);
////        if(size>0) {
////            return ResultGenerator.genSuccessMEssageResult();
////        }
////        return ResultGenerator.genSuccessMEssageResult("更新失败!该用户不存在，userId="+userId);
////    }

    @GetMapping("redis")
    @ApiOperation(value = "重置启动时加载的redis",notes = "重置启动时加载的redis",produces = "application/json")
    public String testRedis() {
        applicationStartup.onApplicationEvent(null);
        return "success";
    }

}
