package com.example.auctionapp.dao;

import com.example.auctionapp.entity.BrowseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface BrowseRecordMapper extends BaseMapper<BrowseRecord> {

    /**
     * 查询客户的历史浏览
     * @param browseRecord
     * @return
     */
    List<Map<String,Object>> selectBrowseRecordByCustomerId(BrowseRecord browseRecord);
}
