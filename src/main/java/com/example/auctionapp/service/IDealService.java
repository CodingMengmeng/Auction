package com.example.auctionapp.service;

import com.example.auctionapp.vo.DealConditionVo;
import com.example.auctionapp.vo.WinRateRequestVo;
import com.example.auctionapp.vo.WinRateResponseVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IDealService {

    /*
    * 根据拍品id查询排中相关参数
    * */
    Map<String, Object> getGoodsDealParamById(String auctionGoodsId);

    /*
    * 根据拍品id，客户id查询是否拍中
    * */
    boolean isDealConclued(String auctionGoodsId) throws Exception;

    public List<DealConditionVo> getdealConditionInfo(String auctionGoodsId);

    public List<WinRateResponseVo> calWinRate(List<WinRateRequestVo> winRateRequestVoList);

}
