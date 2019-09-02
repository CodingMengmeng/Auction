package com.example.auctionapp.service;

import com.example.auctionapp.entity.BrowseRecord;
import com.example.auctionapp.entity.Message;

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
public interface IBrowseRecordService  {

    /**
     * 查询客户的历史浏览
     * @param browseRecord
     * @return
     */
    List<Map<String,Object>> selectBrowseRecordByCustomerId(BrowseRecord browseRecord);


    Integer removeHistory(Message message);
}
