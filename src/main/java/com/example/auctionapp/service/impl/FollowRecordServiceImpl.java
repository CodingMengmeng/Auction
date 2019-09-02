package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.FollowRecordMapper;
import com.example.auctionapp.entity.FollowRecord;
import com.example.auctionapp.service.IFollowRecordService;
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
public class FollowRecordServiceImpl  implements IFollowRecordService {
    @Resource
    private FollowRecordMapper followRecordMapper;

    @Override
    public List<Map<String, Object>> selectFollowRecordByCustomerId(FollowRecord followRecord) {
        return followRecordMapper.selectFollowRecordByCustomerId(followRecord);
    }
}
