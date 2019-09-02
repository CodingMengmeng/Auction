package com.example.auctionapp.core;

public interface RedisConstant {

    /**
     * 实名认证
     */
    String REALNAMECODE = "RealNameCode";

    /**
     * 竞猜活动 排行榜CODE
     */
   String RDS_ACTIVITY_CODE="auction:RDS_ACTIVITY_CODE:";

   String RDS_AUCTION_LOCK_KEY_PREFIX="auction:lock:key:";
}
