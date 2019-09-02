package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.DailySelectionMapper;
import com.example.auctionapp.service.IDailySelectionService;
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
public class DailySelectionServiceImpl  implements IDailySelectionService {
    @Resource
    private DailySelectionMapper dailySelectionMapper;

    /**
     * 查询每日精选
     * @author 马会春
     * @return
     */
    @Override
    public List<Map<String, Object>> selectDailySelection() {
        return dailySelectionMapper.selectDailySelection();
    }
}
