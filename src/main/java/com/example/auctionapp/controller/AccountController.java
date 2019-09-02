package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.Entity;
import java.util.Map;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author 安能
 * @since 2019-05-06
 */

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {


    @Resource
    IAccountService accountService;


    @WebLog(value = "更新密码")
    @DeleteMapping("u/{age}")
    public Result deletePayPassword(@PathVariable("age") @RequestParam(required = false) Map map) {

        return Result.errorInfo("Delete方法:");
    }

    @WebLog(value = "更新密码")
    @GetMapping("u/{age}")
    public Result updatePayPassword(@PathVariable("age") @RequestParam(required = false) Map map) {

        return Result.errorInfo("Get方法");
    }


    /**
     * 修改支付密码
     *
     * @param account 账户表
     * @return
     */
    @WebLog(value = "更新密码")
    @PostMapping("updatePayPassword")
    public Result updatePayPassword(@RequestHeader("userId") Integer userId, Account account) {

        account.setSubjectId(userId);
        return Result.isSuccess(accountService.updatePayPassword(account));
    }

    /**
     * 账户余额查询
     *
     * @param account 账户表
     * @return
     */
    @WebLog("获取当前账户信息")
    @PostMapping("getAccount")
    public Result getAccount(@RequestBody Account account) {
        //参数校验
        if (account == null || account.getSubjectId() == null) {
            return ResultGenerator.genSuccessMEssageResult("参数不全，请确认参数");
        }
        return ResultGenerator.genSuccessResult(accountService.selectAccount(account));
    }

}
