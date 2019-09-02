package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.MarkupRecordMapper;
import com.example.auctionapp.service.IMarkupRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 加价 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class MarkupRecordServiceImpl implements IMarkupRecordService {


    @Resource
    MarkupRecordMapper markupRecordMapper;

    /**
     * 获取最高出价和排名
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public Map<String,Object> getNowMarkupInfo(Integer userId, Integer goodsId) {
        return markupRecordMapper.getNowMarkupInfo(userId,goodsId);
    }

    @Override
    public Map<String,Object> getNowMarkupHisInfo(Integer userId, Integer goodsId) {
        return markupRecordMapper.getNowMarkupHisInfo(userId,goodsId);
    }
}
