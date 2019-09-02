package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.dao.AuctionFieldMapper;
import com.example.auctionapp.entity.AuctionField;
import com.example.auctionapp.service.IAuctionFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专场 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class AuctionFieldServiceImpl implements IAuctionFieldService {

    @Resource
    private AuctionFieldMapper auctionFieldMapper;


    /**
     * 查询拍场信息
     * @param paging
     * @return
     */
    @Override
    public IPage<AuctionField> getAuctionField(Paging paging) {
        IPage<AuctionField> auctionFields = auctionFieldMapper.selectAuctionField(new Page(paging.getPage(),paging.getRows()));
        return auctionFields;
    }

    /**
     * 查询某个拍场的拍品
     * @param paging
     * @return
     */
    @Override
    public IPage<Map<String, Object>> getAuctionFieldOfGoods(Paging paging) {
        IPage<Map<String, Object>> list = auctionFieldMapper.selectAuctionFieldOfGoods(new Page(paging.getPage(), paging.getRows()), paging.getFieldId());
        return list;
    }
}
