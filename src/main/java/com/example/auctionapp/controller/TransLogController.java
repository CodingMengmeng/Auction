package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.ITransLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 交易 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("/transLog")
public class TransLogController {

    @Resource
    ITransLogService transLogService;




    @WebLog("查询出用户的交易记录")
    @PostMapping("selectByType")
    public Result selectByType(@RequestHeader("userId") Integer customerId,
                               @RequestBody Map map) {

        int type = Integer.parseInt(Optional.ofNullable(map.get("type")).orElse("0").toString());
        int size = Integer.parseInt(Optional.ofNullable(map.get("size")).orElse("10").toString());
        int current = Integer.parseInt(Optional.ofNullable(map.get("current")).orElse("1").toString());

        Page<TransLog> page = new Page<>();
        page.setSize(size).setCurrent(current);

        return Result.success(transLogService.selectByType(page, customerId, type));
    }


    /**
     * 用户发起提现申请
     * @param transLog
     * @return
     */
    @WebLog("用户发起提现申请接口")
    @PostMapping("/applyForWithdraw")
    public Result applyForWithdraw(@RequestBody TransLog transLog){
        //log.info("用户发起提现申请接口，接收参数,param --> {}",transLog);
        //交易类型
        if (transLog.getTransType()==null ||
                //交易发起者
                transLog.getSubject()==null ||
                //交易金额
                transLog.getAmount() == null ||
                //余额提现
                StringUtils.isEmpty(transLog.getChannel()) ||
                //支付标记（0支出 1收入）
                transLog.getTransSign() == null ||
                //实际支付金额
                transLog.getPayAmount() == null ||
                //银行卡id
                transLog.getBankId() == null ||
                //银行名称
                StringUtils.isEmpty(transLog.getBankName()) ||
                //银行卡卡号
                StringUtils.isEmpty(transLog.getBankCardNo())
        ){
            return ResultGenerator.genSuccessMEssageResult("参数不全，空值返回");
        }
        if (transLog.getPayAmount().equals(0)|| transLog.getAmount().equals(0)){
            return ResultGenerator.genSuccessMEssageResult("提现金额必须大于0");
        }
        return transLogService.applyForWithdraw(transLog);

    }

    /**
     * 查询交易后的信息
     *
     * @param transLog
     * @return
     */
    @GetMapping("getTransNotify")
    public Result getTransNotify(@RequestHeader("userId") Integer customerId,TransLog transLog) {
        transLog.setSubject(customerId);
        return transLogService.getTransNotify(transLog);
    }

}
