package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.RecommendMapper;
import com.example.auctionapp.service.IRecommendService;
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
public class RecommendServiceImpl  implements IRecommendService {
    @Resource
    private RecommendMapper recommendMapper;

    /**
     * 查询热门推荐
     * @author 马会春
     * @return
     */
    @Override
    public List<Map<String, Object>> selectRecommend() {
        return recommendMapper.selectRecommend();
    }
}
