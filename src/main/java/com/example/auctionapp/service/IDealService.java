package com.example.auctionapp.service;

import com.example.auctionapp.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IDealService {

    /*
    * 根据拍品id查询排中相关参数
    * */
    Map<String, Object> getGoodsDealParamById(int auctionGoodsId);

    /*
    * 根据拍品id，客户id查询是否拍中
    * */
    public DealConcluedVo isDealConclued(int auctionGoodsId) throws Exception;

    //计算拍中几率函数
    public List<WinRateResponseVo> calWinRate(List<WinRateRequestVo> winRateRequestVoList);
    //代理返佣函数
    public int executeAgentCommision(int concluedCustomerId,int goodsId);
    //平台返佣接口
    //输入：拍品编号
    public int executePlatformCommision(int concluedCustomerId,int goodsId);

    //用户返佣接口
    //输入：拍中者id,拍品编号
    public int executeCustomerCommision(int concluedCustomerId,int goodsId);

    //根据用户id、拍品id查询拍中几率
    public BigDecimal getWinRateByCustomerId(int customerId,int goodsId);

    }
