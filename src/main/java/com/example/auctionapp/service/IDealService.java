package com.example.auctionapp.service;

import java.util.Map;

public interface IDealService {

    /*
    * 根据拍品id查询排中相关参数
    * */
    Map<String, Object> getGoodsDealParamById(String auctionGoodsId);


}
