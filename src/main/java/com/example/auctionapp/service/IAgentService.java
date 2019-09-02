package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;

/**
 * <p>
 * 代理 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IAgentService  {
    /**
     * 根据代理id 查询代理详情
     * @return
     */
    Result selectAgentById(Integer agentId);

}
