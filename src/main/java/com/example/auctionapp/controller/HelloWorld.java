package com.example.auctionapp.controller;

import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.vo.CustomerDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description 
 * @author mengjia
 * @date 2019/8/27
 * @version 1.0
 * @see 
 */
@RestController
public class HelloWorld {
    @Autowired
    private ICustomerService customerService;
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Result<CustomerDataVo> helloWorld() {
        CustomerDataVo customerDataVo = new CustomerDataVo();
        customerDataVo.setAddress("浦东新区");
        customerDataVo.setAgentId(11);
        customerDataVo.setBalance(new BigDecimal(345.6));
        return Result.success(customerDataVo);
    }

    /**
     * 根据id查询出用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    @WebLog("根据id查询出用户")
    @PostMapping("hello2")
    public Result selectCustomerAndAccount(@RequestHeader("userId") Integer id) {
        Map map = customerService.selectCustomerAndAccount(id);
        return Result.success(map);
    }
}
