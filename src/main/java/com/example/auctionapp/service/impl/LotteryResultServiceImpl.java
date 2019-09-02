package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.auctionapp.dao.LotteryResultMapper;
import com.example.auctionapp.entity.LotteryResult;
import com.example.auctionapp.service.ILotteryResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽奖结果表 服务实现类
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Service
public class LotteryResultServiceImpl extends ServiceImpl<LotteryResultMapper, LotteryResult> implements ILotteryResultService {

    @Autowired
    private LotteryResultMapper lotteryResultMapper;

    /**
     * 查询分页信息
     *
     * @param startNum
     * @param pageSize
     * @param lotteryResult
     * @return
     */
    @Override
    public Map<String, Object> pageList(int startNum, int pageSize, LotteryResult lotteryResult){
        Map<String, Object> resultMap = new HashMap<>();

        Page<LotteryResult> page = new Page<LotteryResult>();

        QueryWrapper<LotteryResult> wrapper = new QueryWrapper<LotteryResult>();
        // 当前页
        page.setCurrent(startNum);
        // 每页显示数量
        page.setSize(pageSize);

        //活动主键
        if(lotteryResult.getActivityId() !=null ){
            wrapper.eq("activity_id", lotteryResult.getActivityId());
        }

        //客户主键
        if(lotteryResult.getCustomerId() !=null ){
            wrapper.eq("customer_id", lotteryResult.getCustomerId());
        }

        //分页查询
        IPage<LotteryResult> pageList = lotteryResultMapper.selectPage(page, wrapper);
        List<LotteryResult> resultList= pageList.getRecords();

        resultMap.put("rows", resultList);
        resultMap.put("total", pageList.getTotal());

        return  resultMap;
    }

    /**
     *  获取活动的中奖信息
     * @param lotteryResult
     * @return
     */
    @Override
    public List<LotteryResult> getLotteryResultByCode(LotteryResult lotteryResult){
        return  lotteryResultMapper.getLotteryResultList(lotteryResult);
    }
}
