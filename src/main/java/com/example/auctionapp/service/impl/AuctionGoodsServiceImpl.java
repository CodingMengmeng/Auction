package com.example.auctionapp.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.dao.AuctionGoodsMapper;
import com.example.auctionapp.dao.BannerMapper;
import com.example.auctionapp.dao.BrowseRecordMapper;
import com.example.auctionapp.dao.FollowRecordMapper;
import com.example.auctionapp.entity.Banner;
import com.example.auctionapp.entity.BrowseRecord;
import com.example.auctionapp.entity.FollowRecord;
import com.example.auctionapp.dao.*;
import com.example.auctionapp.entity.*;
import com.example.auctionapp.service.IAuctionGoodsService;
import com.example.auctionapp.service.IMarkupRecordHisService;
import com.example.auctionapp.service.IMarkupRecordService;
import com.example.auctionapp.util.DateTimeUtil;
import com.example.auctionapp.vo.AuctionGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.auctionapp.entity.AuctionGoods;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 拍品 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@Service
public class AuctionGoodsServiceImpl implements IAuctionGoodsService {

    @Resource
    private AuctionGoodsMapper auctionGoodsMapper;
    @Resource
    private BannerMapper bannerMapper;
    @Resource
    private BrowseRecordMapper browseRecordMapper;
    @Resource
    private FollowRecordMapper followRecordMapper;
    @Resource
    private GoodsOrderMapper goodsOrderMapper;
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    IMarkupRecordService markupRecordService;

    @Resource
    IMarkupRecordHisService markupRecordHisService;

    @Resource
    AccountMapper accountMapper;

    @Resource
    MarkupRecordMapper markupRecordMapper;
    @Resource
    TransLogMapper transLogMapper;

    /**
     * 查询正在拍卖的拍品
     *
     * @param paging author 马会春
     * @return
     */
    @Override
    public IPage<Map<String, Object>> proceedAuctionGoods(Paging paging) {
        IPage<Map<String, Object>> ipage = new Page<>(paging.getPage(), paging.getRows());
        List<Map<String, Object>> list = auctionGoodsMapper.proceedAuctionGoods(ipage, paging.getName());
        ipage.setRecords(list);
        return ipage;
    }

    /**
     * 客户添加或取消关注
     *
     * @param followRecord
     * @return
     */
    @Override
    public Result addFollow(FollowRecord followRecord) {
        FollowRecord follow1Record = followRecordMapper.selectOne(new QueryWrapper<>(new FollowRecord()).eq("customer_id", followRecord.getCustomerId()).eq("good_id", followRecord.getGoodId()));
        if (follow1Record != null) {
            follow1Record.setFollowFlag(followRecord.getFollowFlag());
            followRecordMapper.updateById(follow1Record);
            if (followRecord.getFollowFlag() == 1) {
                return ResultGenerator.genSuccessResult("关注成功");
            } else {
                return ResultGenerator.genSuccessResult("取消关注");
            }
        }
        followRecord.setStatus(1);
        followRecordMapper.insert(followRecord);
        return ResultGenerator.genSuccessResult("关注成功");
    }

    /**
     * 详情页中已拍下客户信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public Result getSuccessPat(Integer goodsId) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<GoodsOrder> goods = goodsOrderMapper.selectList(new QueryWrapper<>(new GoodsOrder()).eq("goods_id", goodsId));
        if (goods.size() != 1) {
            return ResultGenerator.genSuccessMEssageResult("错误");
        }
        if (goods.get(0).getCustomerId() == 0) {

            List<MarkupRecordHis> markupRecordHisList = markupRecordHisService.list(new QueryWrapper<MarkupRecordHis>()
                    .eq("goods_id", goods.get(0).getId())
                    .eq("user_id", 0));
            map.put("head_portrait", "");

            map.put("phone", markupRecordHisList.get(0).getOrderNumber());
            map.put("name", goods.get(0).getRemark());
            return ResultGenerator.genSuccessResult(map);
        }
        Customer customer = customerMapper.selectById(goods.get(0).getCustomerId());
        map.put("head_portrait", customer.getHeadPortrait());
        map.put("name", customer.getName());
        map.put("phone", customer.getPhone());
        return ResultGenerator.genSuccessResult(map);
    }

    @Override
    public List<Map<String, Object>> getMarkupRecord(AuctionGoods auctionGoods) {
        List<Map<String, Object>> markupRecord = auctionGoodsMapper.getMarkupRecord(auctionGoods);
        markupRecord.forEach((map) -> {

            String createTime = Optional.ofNullable(map.get("create_time").toString()).orElse("");
            String formatDate = DateTimeUtil.formatDate(createTime);
            map.put("create_time", formatDate);
        });
        return markupRecord;
    }

    /**
     * 首页轮播
     */
    @Override
    public IPage<Map<String, Object>> polling() {
        Paging paging = new Paging();
        IPage<Map<String, Object>> ipage = new Page<>(paging.getPage(), paging.getRows());
        List<Map<String, Object>> polling = auctionGoodsMapper.polling(ipage);
        ipage.setRecords(polling);
        return ipage;
    }

    /**
     * 首页轮播图
     *
     * @return
     */
    @Override
    public List<Banner> slideshow() {
        List<Banner> status = bannerMapper.selectList(new QueryWrapper<>(new Banner()).eq("status", 1));
        return status;
    }

    @Override
    public Page<AuctionGoods> selectCustomerCompete(Page<AuctionGoods> page, Integer customerId) {
        List<AuctionGoods> auctionGoods = auctionGoodsMapper.selectCustomerCompete(page, customerId);
        page.setRecords(auctionGoods);
        return page;
    }

    @Override
    public Page<AuctionGoods> selectCustomerAttention(Page<AuctionGoods> page, Integer customerId) {
        List<AuctionGoods> auctionGoods = auctionGoodsMapper.selectCustomerAttention(page, customerId);
        page.setRecords(auctionGoods);
        return page;
    }

    @Override
    public Page<AuctionGoodsVO> selectCustomerInCompete(Page page, Integer customerId) {

        List<AuctionGoodsVO> goodsVoList = auctionGoodsMapper.selectCustomerInCompete(page, customerId);
        for (AuctionGoodsVO auctionGoodsVO : goodsVoList) {
            Map<String, Object> markupInfo = markupRecordService.getNowMarkupInfo(customerId, auctionGoodsVO.getId());
            int rownum = ((Double) markupInfo.get("rownum")).intValue();
            auctionGoodsVO.setRank(rownum);
        }
        System.out.println(goodsVoList);
        return page.setRecords(goodsVoList);
    }

    @Override
    public Page<AuctionGoodsVO> selectCustomerOutCompete(Page page, Integer customerId) {
        List<AuctionGoodsVO> auctionGoodsVOS = auctionGoodsMapper.selectCustomerOutCompete(page, customerId);
        for (AuctionGoodsVO auctionGoodsVO : auctionGoodsVOS) {
            if (new Integer("0").equals(auctionGoodsVO.getOrderCustomerId())) {

                List<MarkupRecordHis> markupRecordHisList = markupRecordHisService.list(new QueryWrapper<MarkupRecordHis>()
                        .eq("goods_id", auctionGoodsVO.getId())
                        .eq("user_id", 0));

                auctionGoodsVO.setPhone(markupRecordHisList.get(0).getOrderNumber());
                auctionGoodsVO.setCustomerName(auctionGoodsVO.getOrderRemark());
            }
            String date = DateUtil.formatDateTime(auctionGoodsVO.getCreateTime());
            String s = DateTimeUtil.formatDate(date);
            auctionGoodsVO.setDealTime(s);
        }
        return page.setRecords(auctionGoodsVOS);
    }

    @Override
    public Page<AuctionGoods> selectCustomerBrowse(Page<AuctionGoods> page, Integer customerId) {
        List<AuctionGoods> auctionGoods = auctionGoodsMapper.selectCustomerBrowse(page, customerId);
        page.setRecords(auctionGoods);
        return page;
    }

    @Override
    public IPage<AuctionGoods> selectByType(Page<AuctionGoods> page, int type, int sort) {

        List<AuctionGoods> auctionGoods = auctionGoodsMapper.selectByType(page, type, sort);
        page.setRecords(auctionGoods);
        return page;
    }


    /**
     * 根据id获取拍品信息
     *
     * @param auctionGoods
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> getGoodsDetailById(AuctionGoods auctionGoods) {
        if (auctionGoods.getCustomerId() == null) {
            return auctionGoodsMapper.getGoodsDetailById(auctionGoods);
        }
        //查询浏览记录，如果为空则插入一条，如果不为空，则修改浏览数量
        BrowseRecord browse1Record = browseRecordMapper.selectOne(new QueryWrapper<>(new BrowseRecord()).eq("goods_id", auctionGoods.getId()).eq("customer_id", auctionGoods.getCustomerId()));
        log.info("查询浏览记录为-->{}", browse1Record);
        if (browse1Record != null) {
            BrowseRecord b = new BrowseRecord();
            b.setNumber(1 + browse1Record.getNumber());
            b.setId(browse1Record.getId());
            browseRecordMapper.updateById(b);
            return auctionGoodsMapper.getGoodsDetailById(auctionGoods);
        }
        BrowseRecord browseRecord = new BrowseRecord();
        browseRecord.setGoodsId(auctionGoods.getId());
        browseRecord.setCustomerId(auctionGoods.getCustomerId());
        browseRecord.setNumber(1);
        browseRecord.setStatus(1);
        browseRecordMapper.insert(browseRecord);
        return auctionGoodsMapper.getGoodsDetailById(auctionGoods);
    }

    @Override
    public AuctionGoods getAuctionGoodsById(int id) {

        return auctionGoodsMapper.selectById(id);
    }

    /**
     * 获取所有正在上拍的拍品Id
     *
     * @return
     */
    @Override
    public List<Integer> getAuctionGoodsOfIdList() {
        return auctionGoodsMapper.selectAuctionGoodsOfIdList();
    }

    @Override
    public List<Map<String, Object>> getRandomAuctionGoods(List<Integer> list) {
        return auctionGoodsMapper.selectRandomAuctionGoods(list);
    }
}
