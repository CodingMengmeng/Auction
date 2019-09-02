package com.example.auctionapp.service.impl;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.dao.AgentMapper;
import com.example.auctionapp.service.IAgentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 代理 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class AgentServiceImpl  implements IAgentService {
    @Resource
    private AgentMapper agentMapper;
    /**
     * 根据代理id 查询代理详情
     * @return
     */
    @Override
    public Result selectAgentById(Integer agentId) {
        return ResultGenerator.genSuccessResult(agentMapper.selectById(agentId));
    }
}
