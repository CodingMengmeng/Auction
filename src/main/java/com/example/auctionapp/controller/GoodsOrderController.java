package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.GoodsOrder;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.IGoodsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 拍品订单 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Api(value = "/goodsOrder/", tags = "订单Api")
@RestController
@RequestMapping("/goodsOrder")
public class GoodsOrderController {


    @Resource
    IGoodsOrderService goodsOrderService;

    /**
     * 查询出客户订单的商品
     *
     * @param
     * @param customerId
     * @return
     */
    @ApiOperation(value = "我的订单接口————我的转卖接口")
    @PostMapping("selectCustomerOrderInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "客户编号"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "page", value = "当前页",example = "1"),
            @ApiImplicitParam(name = "rows", value = "每页显示数量",example = "10")
    })
    public Result selectCustomerOrderInfo(@RequestHeader("userId") Integer customerId, @RequestBody Map map) {
        Integer status = null;
        if (map.get("status") != null) {
            status = Integer.parseInt(map.get("status").toString());
        }
        int size = Integer.parseInt(Optional.ofNullable(map.get("rows")).orElse("10").toString());
        int current = Integer.parseInt(Optional.ofNullable(map.get("page")).orElse("1").toString());
        return Result.success(goodsOrderService.selectCustomerOrderInfo(new Page<>(current, size), customerId, status));
    }

    /**
     * 订单余额支付（）
     *
     * @param transLog
     * @return
     */
    @PostMapping("/orderPayByBalance")
    public Result orderPayByBalance(@RequestBody TransLog transLog) {
        //参数校验
        if (transLog == null ||
                transLog.getOrderNumber() == null ||
                transLog.getGoodsId() == null ||
                transLog.getSubject() == null ||
                transLog.getAmount() == null ||
                transLog.getPayAmount() == null ||
                transLog.getTransType() == null ||
                transLog.getChannel() == null  ||
                transLog.getAddressId() == null
        ) {
            return ResultGenerator.genSuccessMEssageResult("参数不全，请确认");
        }
        if (!transLog.getChannel().equals(ProjectConstant.PAY_CHANNEL_BALANCE)) {
            return ResultGenerator.genSuccessMEssageResult("支付方式不正确");
        }

        return goodsOrderService.orderPayByBalance(transLog);
    }

    @WebLog("更新订单信息")
    @PostMapping("update")
    public Result update(@RequestBody GoodsOrder goodsOrder) {
        return Result.success(goodsOrderService.update(goodsOrder));
    }
}
