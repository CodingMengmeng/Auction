package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.service.ICouponManageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 订单,红包,优惠券关联表 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@RestController
@RequestMapping("/couponManage")
public class CouponManageController {


    @Resource
    ICouponManageService couponManageService;

    @WebLog("根据状态查询用户优惠券信息")
    @PostMapping("selectByStatus")
    public Result selectByStatus( @RequestBody Map map, @RequestHeader("userId") Integer customerId) {

        int status = Integer.parseInt(Optional.ofNullable(map.get("status")).orElse("0").toString());
        int size = Integer.parseInt(Optional.ofNullable(map.get("size")).orElse("10").toString());
        int current = Integer.parseInt(Optional.ofNullable(map.get("current")).orElse("1").toString());
        return Result.success(couponManageService.selectByStatus(new Page<>(current,size), customerId, status));
    }
}
