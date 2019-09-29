package com.example.auctionapp.controller;

import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.service.IDealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/deal")
public class DealController {
    @Resource IDealService iDealService;

    @WebLog("查询拍中条件接口")
    @RequestMapping("/getGoodsDealParamById")
    public Result getGoodsDealParamById(@RequestHeader("auctionGoodsId")  String auctionGoodsId) {
        System.out.println("rest:"+auctionGoodsId);
        return Result.success(iDealService.getGoodsDealParamById(auctionGoodsId));
    }

    @RequestMapping("/hello")
    public Result hello(String id) {
        return Result.success("hello World:"+id);
    }

    @RequestMapping("/getGoodsIfDealById")
    public Result getGoodsIfDealById(@RequestHeader("auctionGoodsId")  String auctionGoodsId) {
        return Result.success(iDealService.getdealConditionInfo(auctionGoodsId));
    }

    @RequestMapping("/getIsDealConcluedById")
    public Result getIsDealConcluedById(@RequestHeader("auctionGoodsId")  String auctionGoodsId) {
        try {
            return Result.success(iDealService.isDealConclued(auctionGoodsId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
