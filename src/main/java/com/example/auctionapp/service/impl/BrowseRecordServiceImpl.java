package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.dao.BrowseRecordMapper;
import com.example.auctionapp.entity.BrowseRecord;
import com.example.auctionapp.entity.Message;
import com.example.auctionapp.service.IBrowseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class BrowseRecordServiceImpl  implements IBrowseRecordService {
    @Resource
    private BrowseRecordMapper browseRecordMapper;

    /**
     * 查询客户的历史浏览
     * @param browseRecord
     * @return
     */
    @Override
    public List<Map<String, Object>> selectBrowseRecordByCustomerId(BrowseRecord browseRecord) {

        return browseRecordMapper.selectBrowseRecordByCustomerId(browseRecord);
    }


    /**
     * 删除某个客户下的浏览历史记录
     * @param message
     * @return
     */
    @Override
    public Integer removeHistory(Message message) {
        int customerId = browseRecordMapper.delete(new QueryWrapper<>(new BrowseRecord()).eq("customer_id", message.getSubjectId()));
        return customerId;
    }
}
