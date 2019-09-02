package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auctionapp.dao.MarkupRecordHisMapper;
import com.example.auctionapp.entity.MarkupRecordHis;
import com.example.auctionapp.service.IMarkupRecordHisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 加价历史 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class MarkupRecordHisServiceImpl extends ServiceImpl<MarkupRecordHisMapper, MarkupRecordHis> implements IMarkupRecordHisService {

    @Resource
    MarkupRecordHisMapper markupRecordHisMapper;


}
