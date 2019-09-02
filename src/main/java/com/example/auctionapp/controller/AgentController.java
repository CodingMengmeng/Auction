package com.example.auctionapp.controller;


import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.service.IAgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 代理 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    @Resource
    private IAgentService iAgentService;

    /**
     * 根据代理Id查询代理详情接口
     * @return
     */
    @WebLog("根据代理Id查询代理详情接口")
    @PostMapping("/selectAgentById")
    public Result selectAgentById(Integer agentId){
        return iAgentService.selectAgentById(agentId);
    }

}
