package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Bank;
import com.example.auctionapp.service.IBankService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 银行卡 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@RestController
@RequestMapping("/bank")
public class BankController {


    @Resource
    IBankService bankService;

    /**
     * 增加
     *
     * @param bank
     * @return
     */
    @WebLog("增加银行卡")
    @PostMapping("insert")
    public Result insert(@RequestHeader("userId") Integer userId,@RequestBody Bank bank) {

        bank.setUserId(userId);
        return bankService.insert(bank);
    }

    /**
     * 删除
     *
     * @param id
     * @re
     * @PostMapping("turn
     */
    @WebLog("删除银行卡")
    @PostMapping("delete")
    public Result delete(@RequestBody Map<String, Integer> map) {
        Integer id = Optional.ofNullable(map.get("id")).orElse(-1);

        return bankService.delete(id);
    }

    /**
     * 修改
     *
     * @param bank
     * @return
     */
    @WebLog("修改银行卡")
    @PostMapping("update")
    public Result update(@RequestBody Bank bank) {

        return bankService.update(bank);
    }

    /**
     * 根据id查询出一个
     *
     * @param  map id
     * @return
     */
    @WebLog("根据id查询出一个银行卡")
    @PostMapping("selectById")
    public Result selectById(@RequestBody Map<String, Integer> map) {
        Integer id = Optional.ofNullable(map.get("id")).orElse(-1);
        return Result.success(bankService.selectById(id));
    }

    /**
     * 根据客户id查询出多个
     *
     * @param userId
     * @param map acctType
     * @return
     */
    @WebLog("根据客户id查询出多个银行卡")
    @PostMapping("selectList")
    public Result selectList(@RequestHeader("userId") Integer userId, @RequestBody Map<String, Integer> map) {
        Integer acctType = Optional.ofNullable(map.get("acctType")).orElse(-1);
        return Result.success(bankService.selectList(userId, acctType));
    }
}
