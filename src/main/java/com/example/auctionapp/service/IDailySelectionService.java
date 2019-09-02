package com.example.auctionapp.service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IDailySelectionService  {

    /**
     * 查询每日精选
     * @author 马会春
     * @return
     */
    List<Map<String,Object>> selectDailySelection();

}
