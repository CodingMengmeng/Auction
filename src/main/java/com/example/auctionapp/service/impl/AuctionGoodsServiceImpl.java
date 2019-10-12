package com.example.auctionapp.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.ProjectConstant;
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
import com.example.auctionapp.entity.ext.MarkupRecordClassify;
import com.example.auctionapp.entity.ext.MarkupRecordSummary;
import com.example.auctionapp.service.IAuctionGoodsService;
import com.example.auctionapp.service.IDealService;
import com.example.auctionapp.service.IMarkupRecordHisService;
import com.example.auctionapp.service.IMarkupRecordService;
import com.example.auctionapp.util.CalcUtils;
import com.example.auctionapp.util.DateTimeUtil;
import com.example.auctionapp.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.example.auctionapp.entity.AuctionGoods;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    @Resource
    private AuctionValueMapper auctionValueMapper;

    @Resource
    private BadgeCustomerMapper badgeCustomerMapper;

    @Resource
    private RankingListMapper rankingListMapper;

    @Resource
    private MarkupRecordHisMapper markupRecordHisMapper;

    @Resource
    private InPatMapper inPatMapper;

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private ShareHistoryMapper shareHistoryMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private IDealService iDealService;

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

    /**
     * @description 判断出价是否成功，私有函数
     * @author mengjia
     * @date 2019/10/11
     * @param bidInfoVo 出价信息
     * @param proportion 佣金比例
     * @return boolean
     * @throws
     **/
    private boolean isBidSuccess(BidInfoVo bidInfoVo, BigDecimal proportion ){
        int subjectId = bidInfoVo.getCustomerId();
        BigDecimal bidPrice = bidInfoVo.getBidPrice();
        BigDecimal commission = bidPrice.multiply(proportion);
        Map<String,Object> result = accountMapper.selectBalanceAndWithBeansById(subjectId);
        BigDecimal balance;
        BigDecimal withBeans;
        if( result.get("balance") == null){
            balance = CalcUtils.ZERO;
        }else{
            balance = (BigDecimal) result.get("balance");
        }
        if(result.get("with_beans") == null){
            withBeans = CalcUtils.ZERO;
        }else{
            withBeans = (BigDecimal) result.get("with_beans");
        }
        log.info("balance:" + balance);
        log.info("withBeans:" + withBeans);
        if((balance.add(withBeans)).compareTo(commission) > -1){
            //出价成功，减去该用户的余额
            Account account = new Account();
            account.setBalance(balance.subtract(bidInfoVo.getActualPayBeans()));
            account.setWithBeans(withBeans.subtract(bidInfoVo.getMortgageFreeBean()));
            accountMapper.updateBalanceAndWithBeansById(account);
            return true;
        }else{
            return false;
        }
    }

    /**
     * @description 获取用户徽章信息，包括徽章类型、等级编号、等级、豆量
     * @author mengjia
     * @date 2019/9/18
     * @param subjectId 用户id
     * @return java.util.List<com.example.auctionapp.vo.BadgeCustomerVo>
     * @throws
     **/
    private List<BadgeCustomerVo> getCustomerBadgeInfos(int subjectId){
        return badgeCustomerMapper.selectBadgeInfosById(subjectId);
    }
    /**
     * @description 更新或新增拍卖值信息，包括拍卖值表和排行表
     * @author mengjia
     * @date 2019/9/18
     * @param bidInfoVo 出价信息
     * @return java.lang.String
     * @throws
     **/
    private void updateOrInsertAuctionValueData(BidInfoVo bidInfoVo,List<BadgeCustomerVo> badgeCustomerInfos){
        //根据用户id和拍品id获取拍卖值信息
        AuctionValue auctionValueVo = auctionValueMapper.selectAuctionValueInfoById(bidInfoVo.getCustomerId(),bidInfoVo.getGoodsId());
        //根据用户id和拍品id获取排行信息
        RankingList rankingListVo = rankingListMapper.selectByGoodsIdAndCustomerId(bidInfoVo.getGoodsId(),bidInfoVo.getCustomerId());
        //初始化贡献徽章系数和好友徽章系数为1.00
        BigDecimal ctrbBadgeCoefficient = CalcUtils.BADGE_COEFFICIENT_BASIC;
        BigDecimal friendBadgeCoefficient = CalcUtils.BADGE_COEFFICIENT_BASIC;
        if(badgeCustomerInfos != null && badgeCustomerInfos.size() > 0) {
            for(BadgeCustomerVo badgeCustomerInfo : badgeCustomerInfos){
                //贡献徽章系数和好友徽章系数赋值
                if(badgeCustomerInfo.getName().equals("贡献徽章")){
                    ctrbBadgeCoefficient = CalcUtils.calcCtrbBadgeCoefficient(badgeCustomerInfo.getBeans());
                }else if(badgeCustomerInfo.getName().equals("好友徽章")){
                    friendBadgeCoefficient = CalcUtils.calcFriendBadgeCoefficient(badgeCustomerInfo.getBeans());
                }else{
                    log.info("Other types of badges,do nothing.");
                }
            }
        }
        //拍卖值表中无记录，则拍卖值表和排行表中新增记录；否则更新记录
        if(auctionValueVo == null){
            //新增拍卖值表记录

            auctionValueVo = new AuctionValue();
            //用户编号
            auctionValueVo.setCustomerId(bidInfoVo.getCustomerId());
            //拍品编号
            auctionValueVo.setGoodsId(bidInfoVo.getGoodsId());
            //拍卖值
            auctionValueVo.setCustomerValue(CalcUtils.calcAuctionValue(
                    bidInfoVo.getShouldPayBeans(),
                    ctrbBadgeCoefficient,
                    friendBadgeCoefficient
            ));
            //贡献徽章加成点数
            auctionValueVo.setContributeBadgeValue((ctrbBadgeCoefficient.subtract(CalcUtils.BADGE_COEFFICIENT_BASIC))
                                                        .multiply(bidInfoVo.getShouldPayBeans()));
            //好友徽章加成点数
            auctionValueVo.setFriendBadgeValue((friendBadgeCoefficient.subtract(CalcUtils.BADGE_COEFFICIENT_BASIC))
                                                        .multiply(bidInfoVo.getShouldPayBeans()));
            //最高出价
            auctionValueVo.setBid(bidInfoVo.getBidPrice());
            //出价次数
            auctionValueVo.setNum(1);
            //消耗总拍豆，若本次未消耗拍豆，则存入0.00
            auctionValueVo.setConsumeBeans(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
            //消耗总赠豆，若本次未消耗赠豆，则存入0.00
            auctionValueVo.setConsumeGive(Optional.ofNullable(bidInfoVo.getMortgageFreeBean()).orElse(new BigDecimal("0.00")));
            //创建时间
            auctionValueVo.setCreateTime(LocalDateTime.now());
            //更新时间
            auctionValueVo.setUpdateTime(LocalDateTime.now());
            //乐观锁，为1
            auctionValueVo.setVersion(1);
            auctionValueMapper.insert(auctionValueVo);

            //新增排行表记录

            rankingListVo = new RankingList();
            //排名 rank，首次新增则排名为1
            rankingListVo.setRank(1);
            //客户 customer_id
            rankingListVo.setCustomerId(bidInfoVo.getCustomerId());
            //拍品 goods_id
            rankingListVo.setGoodsId(bidInfoVo.getGoodsId());
            //拍卖值 goods_value
            rankingListVo.setGoodsValue(CalcUtils.calcAuctionValue(
                    bidInfoVo.getShouldPayBeans(),
                    ctrbBadgeCoefficient,
                    friendBadgeCoefficient));
            rankingListVo.setCreateTime(LocalDateTime.now());
            rankingListVo.setUpdateTime(LocalDateTime.now());
            rankingListVo.setVersion(1);
            rankingListMapper.insert(rankingListVo);
        }else{
            //更新拍卖值表

            AuctionValue auctionValuePo = new AuctionValue();
            //克隆拍卖值实体bean
            BeanUtils.copyProperties(auctionValueVo,auctionValuePo);
            //拍卖值累加
            auctionValuePo.setCustomerValue(auctionValueVo.getCustomerValue().add(
                    CalcUtils.calcAuctionValue(
                            bidInfoVo.getShouldPayBeans(),
                            ctrbBadgeCoefficient,
                            friendBadgeCoefficient
                    )));
            //累加贡献徽章加成点数
            auctionValuePo.setContributeBadgeValue((ctrbBadgeCoefficient.subtract(CalcUtils.BADGE_COEFFICIENT_BASIC))
                    .multiply(bidInfoVo.getShouldPayBeans()).add(auctionValueVo.getContributeBadgeValue()));
            //累加好友徽章加成点数
            auctionValuePo.setFriendBadgeValue((friendBadgeCoefficient.subtract(CalcUtils.BADGE_COEFFICIENT_BASIC))
                    .multiply(bidInfoVo.getShouldPayBeans()).add(auctionValueVo.getFriendBadgeValue()));
            //最高出价，当前出价与原出价相比，若大于则更新
            auctionValuePo.setBid(bidInfoVo.getBidPrice().compareTo(auctionValueVo.getBid()) > 0 ? bidInfoVo.getBidPrice() : auctionValueVo.getBid());
            //出价次数累加
            auctionValuePo.setNum(auctionValueVo.getNum().intValue() + 1);
            //消耗总拍豆，累加后更新
            auctionValuePo.setConsumeBeans(auctionValueVo.getConsumeBeans().add(bidInfoVo.getActualPayBeans()));
            //消耗总赠豆，累加后更新
            auctionValuePo.setConsumeGive(auctionValueVo.getConsumeGive().add(Optional.ofNullable(bidInfoVo.getMortgageFreeBean()).orElse(new BigDecimal("0.00"))));
            //更新时间
            auctionValuePo.setUpdateTime(LocalDateTime.now());
            auctionValueMapper.updateAuctionValueData(auctionValuePo);

            //更新排行表记录
            RankingList rankingListPo = new RankingList();
            //克隆排行表实体bean
            BeanUtils.copyProperties(rankingListVo,rankingListPo);
            //拍卖值累加
            rankingListPo.setGoodsValue(rankingListVo.getGoodsValue().add(
                    CalcUtils.calcAuctionValue(
                            bidInfoVo.getShouldPayBeans(),
                            ctrbBadgeCoefficient,
                            friendBadgeCoefficient
                    )));
            //更新排行表中的拍卖值
            rankingListMapper.updateGoodsValue(rankingListPo);
        }
        //重新更新排行，并更新拍中几率
        //1、获取该拍品的所有记录（已按拍卖值由大到小排序）
        List<RankingList> rankingLists = rankingListMapper.selectByGoodsId(bidInfoVo.getGoodsId());
        //2、计算拍中几率
        List<WinRateRequestVo> winRateRequestVos = auctionValueMapper.selectAuctionValueInfoByGoodsId(bidInfoVo.getGoodsId());
        log.info(winRateRequestVos.toString());
        List<WinRateResponseVo> winRateResponseVos = iDealService.calWinRate(winRateRequestVos);
        //3、根据拍品ID和客户ID依次更新排行rank字段和win_rate字段
        for(int seq = 1;seq <= rankingLists.size();seq++){
            rankingLists.get(seq - 1).setRank(seq);
            rankingLists.get(seq - 1).setUpdateTime(LocalDateTime.now());
            rankingLists.get(seq -1).setWinRate(winRateResponseVos.get(seq - 1).getWinRate());
            rankingListMapper.updateRank(rankingLists.get(seq - 1));
        }
    }

    /**
     * @description 更新badge_customer表中的已消费拍豆
     * @author mengjia
     * @date 2019/9/23
     * @param emblemId 徽章等级编号
     * @param subjectId 用户ID
     * @param payBeans 本次消费的拍豆
     * @param currentBeans 当前已消费的拍豆
     * @return void
     * @throws
     **/
    private void updateCustomerBadgeBeans(int emblemId,int subjectId,
                                         BigDecimal payBeans,BigDecimal currentBeans,int emblemType){
        BigDecimal newBeans = currentBeans.add(payBeans);
        int newEmblemId = 0;
        //1-好友徽章
        //2-贡献徽章
        if(emblemType == 1){
            newEmblemId = CalcUtils.friendEmblemLevelIdMap.get(CalcUtils.calcFriendBadgeCoefficient(newBeans));
        }else {
            newEmblemId = CalcUtils.ctrbEmblemLevelIdMap.get(CalcUtils.calcCtrbBadgeCoefficient(newBeans));
        }
        BadgeCustomerVo badgeCustomerVo = new BadgeCustomerVo();
        badgeCustomerVo.setEmblemId(emblemId);
        badgeCustomerVo.setCustomerId(subjectId);
        badgeCustomerVo.setBeans(newBeans);
        badgeCustomerVo.setNewEmblemId(newEmblemId);
        int effectNum = badgeCustomerMapper.updateCustomerctrbBadgeBeans(badgeCustomerVo);
        log.info("更新" + effectNum + "条数据");
    }

    /**
     * @description badge_customer表中新增记录
     * @author mengjia
     * @date 2019/10/9
     * @param subjectId 用户ID
     * @param payBeans 消耗拍豆
     * @param emblemType 徽章类型
     * @return void
     * @throws
     **/
    private void insertCustomerBadgeBeans(int subjectId,
                                          BigDecimal payBeans,int emblemType){
        //根据徽章类型获取获取徽章等级编号
        //1-好友徽章；2-贡献徽章
        int emblemId = 0;
        if(emblemType == 1){
            emblemId = CalcUtils.friendEmblemLevelIdMap.get(CalcUtils.calcFriendBadgeCoefficient(payBeans));
        }else{
            emblemId = CalcUtils.ctrbEmblemLevelIdMap.get(CalcUtils.calcCtrbBadgeCoefficient(payBeans));
        }
        BadgeCustomer badgeCustomer = new BadgeCustomer();
        //设置徽章等级编号
        badgeCustomer.setEmblemId(emblemId);
        //设置用户ID
        badgeCustomer.setCustomerId(subjectId);
        //设置消耗拍豆
        badgeCustomer.setBeans(payBeans);
        badgeCustomerMapper.insert(badgeCustomer);

    }

    /**
     * @description 入库加价表
     * @author mengjia
     * @date 2019/9/28
     * @param bidInfoVo
     * @return void
     * @throws
     **/
    private void insertMarkupRecord(BidInfoVo bidInfoVo,MarkupRecordSummary markupRecordSummary) {

        MarkupRecord markupRecord = new MarkupRecord();
        //公共部分赋值
        //订单编号 order_number
        markupRecord.setOrderNumber(ProjectConstant.GOODS_AUCTION + DateTimeUtil.getNowInSS() + bidInfoVo.getCustomerId());
        //商品ID goods_id
        markupRecord.setGoodsId(bidInfoVo.getGoodsId());
        //当前出价 current_bid
        markupRecord.setCurrentBid(bidInfoVo.getBidPrice());
        //用户ID user_id
        markupRecord.setUserId(bidInfoVo.getCustomerId());
        //佣金 commission
        markupRecord.setCommission(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
        //佣金状态 cms_status
        markupRecord.setCmsStatus(1);
        //优惠金额 coupons
        markupRecord.setCoupons(CalcUtils.ZERO);
        //优惠类型 coupons_type
        markupRecord.setCouponsType(0);
        //支付方式 pay_amount
        markupRecord.setPayType("balance");
        //状态 status
        markupRecord.setStatus(1);
        //创建时间 create_time
        markupRecord.setCreateTime(LocalDateTime.now());
        //更新时间 update_time
        markupRecord.setUpdateTime(LocalDateTime.now());
        //乐观锁标志 version
        markupRecord.setVersion(0);

        //可变部分赋值
        /**
         * 按商品ID查询加价表记录，若查询结果为空，
         *      说明该商品无加价记录，直接以当前拍卖商品和出价用户新增一条加价记录
         * 若查询结果不为空，则按商品ID和用户ID查询加价表记录，若查询结果为空，
         *      说明该商品有加价记录，但该用户为首次出价，新增一条该用户的加价记录
         * 若查询结果不为空，则按商品ID和用户ID新增一条加价表记录，注意参拍总次数和用户参拍次数的字段入库
         *
         **/
        if (markupRecordSummary == null) {
            //参拍总次数 total_number
            markupRecord.setTotalNumber(1);
            //用户参拍次数 shooting_times
            markupRecord.setShootingTimes(1);
//            //定金金额/保证金 bond
//            //用户首次参拍时需缴纳，与支付佣金相同
//            markupRecord.setBond(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
        }else{
            //获取该商品ID的参拍总次数
            int totalNumber = markupRecordSummary.getMaxTotalNumber();
            markupRecordSummary = markupRecordMapper.selectSummaryByGoodsIdAndUserId(bidInfoVo.getGoodsId(),bidInfoVo.getCustomerId());
            //参拍总次数 total_number
            markupRecord.setTotalNumber(totalNumber + 1);
            if(markupRecordSummary == null){
                //用户参拍次数 shooting_times
                markupRecord.setShootingTimes(1);
//                //定金金额/保证金 bond
//                markupRecord.setBond(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
            }else{
                //获取该用户该商品的已参拍次数
                int shootingTimes = markupRecordSummary.getMaxShootingTimes();
                //用户参拍次数 shooting_times
                markupRecord.setShootingTimes(shootingTimes + 1);
//                //定金金额/保证金 bond
//                markupRecord.setBond(CalcUtils.ZERO);
            }
        }

        //实际支付金额：支付佣金 - 优惠金额 pay_amount
        markupRecord.setPayAmount(markupRecord.getCommission().subtract(markupRecord.getCoupons()));
        log.info("入库加价记录：" + markupRecord);
        markupRecordMapper.insert(markupRecord);
    }

    /**
     * @description 入库交易流水表
     * @author mengjia
     * @date 2019/9/28
     * @param bidInfoVo
     * @return void
     * @throws
     **/
    private void insertTransLog(BidInfoVo bidInfoVo) {
        TransLog transLog = new TransLog();
        //订单编号 order_number
        transLog.setOrderNumber(ProjectConstant.GOODS_AUCTION + DateTimeUtil.getNowInSS() + bidInfoVo.getCustomerId());
        //商品ID goods_id
        transLog.setGoodsId(bidInfoVo.getGoodsId());
        //交易发起者 subject
        transLog.setSubject(bidInfoVo.getCustomerId());
        Account account = accountMapper.selectAccountInfoBySubjectId(bidInfoVo.getCustomerId());
        if(account != null){
            //账户类型 account_type
            transLog.setAccountType(account.getType());
            //账户ID account_id
            transLog.setAccountId(account.getId());
        }else{
            log.warn("The account info of " + bidInfoVo.getCustomerId() + "could not be queried from the table Account.");
            //交易状态 status，成功入 1-成功，异常入2-失败
            transLog.setStatus(2);
        }
        //交易通道 channel，入"balance"，表示拍豆
        transLog.setChannel("balance");
        //支付标记 trans_sign，入0-支出
        transLog.setTransSign(0);
        //交易类型 trans_type，入5-支付佣金
        transLog.setTransType(5);
        //当前出价 current_bid
        transLog.setCurrentBid(bidInfoVo.getBidPrice());
        //佣金 commission，入消耗的拍豆，没有消耗拍豆，记录为0.00
        transLog.setCommission(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
        //交易金额 amount，入消耗的拍豆，没有消耗拍豆，记录为0.00
        transLog.setAmount(Optional.ofNullable(bidInfoVo.getActualPayBeans()).orElse(new BigDecimal("0.00")));
        //实际支付金额 pay_amount，实际上支付的金额，来记录每一笔的实际出入金，入0.00
        transLog.setPayAmount(CalcUtils.ZERO);
        //交易状态 status，成功入 1-成功，异常入2-失败
        transLog.setStatus(1);
        //赠豆 with_beans，记录每笔消耗的赠豆，没有消耗赠豆时记录为0.00
        transLog.setWithBeans(Optional.ofNullable(bidInfoVo.getMortgageFreeBean()).orElse(new BigDecimal("0.00")));
        //支付类型 channel_type，1-拍豆；2-赠豆；3-拍豆和赠豆
        /**
         * 如果消耗拍豆和消耗赠豆都大于0，则支付类型为3-拍豆和赠豆
         * 如果消耗拍豆大于0，消耗赠豆等于0，则支付类型为1-拍豆
         * 如果消耗拍豆等于0，消耗赠豆大于0，则支付类型为2-赠豆
         * 其余为异常情况
         **/

        if(bidInfoVo.getActualPayBeans().compareTo(CalcUtils.ZERO) > 0
                && bidInfoVo.getMortgageFreeBean().compareTo(CalcUtils.ZERO) > 0){
            transLog.setChannelType(3);
        }else if(bidInfoVo.getActualPayBeans().compareTo(CalcUtils.ZERO) > 0
                && bidInfoVo.getMortgageFreeBean().compareTo(CalcUtils.ZERO) == 0){
            transLog.setChannelType(1);
        }else if(bidInfoVo.getActualPayBeans().compareTo(CalcUtils.ZERO) == 0
                && bidInfoVo.getMortgageFreeBean().compareTo(CalcUtils.ZERO) > 0){
            transLog.setChannelType(2);
        }else{
            log.warn("Commission and with beans are illegal,please check.\n"
                    + "Commission = " + bidInfoVo.getActualPayBeans() + ","
                    + "withBeans = " + bidInfoVo.getMortgageFreeBean() + ".");
            transLog.setStatus(2);
        }

        transLogMapper.insert(transLog);
    }

    /**
     * @description 处理拍中后的入库逻辑，包括订单表、拍品表、中拍记录表、消息表
     * @author mengjia
     * @date 2019/10/7
     * @param goodsId 拍品编号
     * @param customerId 拍中用户编号
     * @param name 拍中用户名字
     * @return void
     * @throws
     **/
    private void dealWinnerStorage(Integer goodsId, Integer customerId,String name){
        //入库订单表

        //取出加价表中的分类汇总信息，包括：
        //最高出价、参拍总次数、总佣金-消耗拍豆、用户参拍总次数、
        //总赠豆、总拍豆
        MarkupRecordClassify markupRecordClassify = markupRecordMapper.selectClassifyByGoodsIdAndUserId(goodsId,customerId);
        //取出拍卖值表中的信息
        //包括拍中者的消耗拍豆、消耗赠豆
        AuctionValue auctionValueVo = auctionValueMapper.selectAuctionValueInfoById(customerId,goodsId);
//        if(markupRecordClassify == null
//                || auctionValueVo == null){
//            throw new RuntimeException("加价表记录或拍卖值表记录为空，拍品编号为" + bidInfoVo.getGoodsId()
//            + ",用户编号为" + bidInfoVo.getCustomerId());
//        }
        //入库订单表
        GoodsOrder goodsOrder = new GoodsOrder();
        //订单编号 order_number
        goodsOrder.setOrderNumber(ProjectConstant.GOODS_ORDER + DateTimeUtil.getNowInSS() + customerId);
        //拍中者ID customer_id
        goodsOrder.setCustomerId(customerId);
        //拍品编号 goods_id
        goodsOrder.setGoodsId(goodsId);
        //参拍次数 total_number
        goodsOrder.setTotalNumber(markupRecordClassify.getMaxShootingTimes());
        //总参拍次数 total_number_all
        goodsOrder.setTotalNumberAll(markupRecordClassify.getMaxTotalNumber());
        //总参拍佣金 reference_commission_all
        goodsOrder.setReferenceCommissionAll(markupRecordClassify.getSumCommission());
        //参拍佣金-消耗拍豆 reference_commission
        goodsOrder.setReferenceCommission(auctionValueVo.getConsumeBeans());
        //优惠金额-消耗赠豆 preferential_amount
        goodsOrder.setPreferentialAmount(auctionValueVo.getConsumeGive());
        //尾款金额-喊价金额 tail_amount
        //取拍卖值表中的最高出价
        goodsOrder.setTailAmount(auctionValueVo.getBid());
        //状态 status 0-待支付
        goodsOrder.setStatus(0);
        //总赠豆 sum_with_beans
        goodsOrder.setSumWithBeans(markupRecordClassify.getSumWithBeans());
        //总拍豆 sum_beans
        goodsOrder.setSumBeans(markupRecordClassify.getSumBeans());

        //获取拍品表信息
        AuctionGoods auctionGoods = auctionGoodsMapper.selectById(goodsId);
        log.info(auctionGoods.toString());
        int rounds = auctionGoods.getRounds() + 1;
        auctionGoods.setRounds(rounds);
        //拍品表中指定拍品的轮次 + 1
        auctionGoodsMapper.updateRoundsById(auctionGoods);
        //轮次 rounds
        goodsOrder.setRounds(rounds);
        //是否转拍 is_shipments 0-待操作
        goodsOrder.setIsShipments(0);
        //转卖剩余次数 resell_size 固定为3次
        goodsOrder.setResellSize(3);
        goodsOrderMapper.insert(goodsOrder);

        //入库中拍记录表
        InPat inPat = new InPat();
        //客户编号 customer_id
        inPat.setCustomerId(customerId);
        //拍品编号 goods_id
        inPat.setGoodsId(goodsId);
        //出价金额 bid_price
        //取拍卖值表中的最高出价
        inPat.setBidPrice(auctionValueVo.getBid());
        //支付拍豆 consume_beans
        inPat.setConsumeBeans(auctionValueVo.getConsumeBeans());
        //支付赠豆 consume_give
        inPat.setConsumeGive(auctionValueVo.getConsumeGive());
        //拍卖值 customer_value
        inPat.setCustomerValue(auctionValueVo.getCustomerValue());
        //好友助力 friend_help
        inPat.setFriendHelp(auctionValueVo.getFriendValue());
        //创建时间 create_time
        inPat.setCreateTime(LocalDateTime.now());
        //修改时间 update_time
        inPat.setUpdateTime(LocalDateTime.now());
        inPatMapper.insert(inPat);

        //入库消息表
        Message message = new Message();
        //系统消息

        //账户类型 type 1-系统消息
        message.setType(1);
        //拍品ID goods_id
        message.setGoodsId(goodsId);
        //主体id subject_id
        message.setSubjectId(customerId);
        //消息内容 message_info
        message.setMessageInfo("用户" + name + "以" + auctionValueVo.getBid()
                + "的价格拍中了" + auctionGoods.getGoodsName() + "!");
        //发送标志 0-未发送
        message.setSendFlag(0);
        //读标志 0-未读
        message.setReadFlag(0);
        //账户状态 1-生效
        message.setStatus(1);
        messageMapper.insert(message);

        //用户消息

        //系统消息
        //账户类型 type 2-用户消息
        message.setType(2);
        //拍品ID goods_id
        message.setGoodsId(goodsId);
        //主体id subject_id
        message.setSubjectId(customerId);
        //消息内容 message_info
        message.setMessageInfo("恭喜您，竞拍成功！");
        //发送标志 0-未发送
        message.setSendFlag(0);
        //读标志 0-未读
        message.setReadFlag(0);
        //账户状态 1-生效
        message.setStatus(1);
        messageMapper.insert(message);
    }
    /**
     * @description 处理拍中后的结转，包括加价表的清空和结转、好友助力拍卖值表的清空和结转、拍卖值表的清空，排行表的清空
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param customerId 拍中用户编号
     * @return void
     * @throws
     **/
    private void dealWinnerCarryforward(Integer goodsId, Integer customerId) {
        //处理加价表
        //结转至历史表
        int affectedNum;
        MarkupRecordHis markupRecordHis = new MarkupRecordHis();
        markupRecordHis.setGoodsId(goodsId);
        markupRecordHis.setUserId(customerId);
        affectedNum = markupRecordHisMapper.winnerCarryforward(goodsId,customerId);
        log.info("加价表结转：" + "已结转拍中者" + customerId + "的" + affectedNum + "条记录至历史表。");
        //清空
        markupRecordMapper.deleteByGoodsIdAndUserId(goodsId,customerId);

        //处理好友助力拍卖值表
        //结转至历史表
        affectedNum = shareHistoryMapper.winnerCarryforward(goodsId,customerId);
        log.info("好友助力拍卖值表结转：" + "已结转拍中者" + customerId + "的" + affectedNum + "条记录至历史表。");
        //清空
        shareMapper.deleteByGoodsIdAndCustomerId(goodsId,customerId);

        //处理拍卖值表
        //清空
        affectedNum = auctionValueMapper.deleteByGoodsIdAndCustomerId(goodsId,customerId);
        log.info("已删除拍卖值表" + customerId + "的" + affectedNum + "条记录");

        //处理排行表
        //清空
        affectedNum = rankingListMapper.deleteByGoodsIdAndCustomerId(goodsId,customerId);
        log.info("已删除排行表" + customerId + "的" + affectedNum + "条记录");
        //重新计算排行
        //1、获取该拍品的所有记录（已按拍卖值由大到小排序）
        List<RankingList> rankingLists = rankingListMapper.selectByGoodsId(goodsId);
        //2、计算拍中几率
        List<WinRateRequestVo> winRateRequestVos = auctionValueMapper.selectAuctionValueInfoByGoodsId(goodsId);
        List<WinRateResponseVo> winRateResponseVos = iDealService.calWinRate(winRateRequestVos);
        //3、根据拍品ID和客户ID依次更新排行rank字段
        for(int seq = 1;seq <= rankingLists.size();seq++){
            rankingLists.get(seq - 1).setRank(seq);
            rankingLists.get(seq - 1).setUpdateTime(LocalDateTime.now());
            rankingLists.get(seq -1).setWinRate(winRateResponseVos.get(seq - 1).getWinRate());
            rankingListMapper.updateRank(rankingLists.get(seq - 1));
        }
    }
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result processBid(BidInfoVo bidInfoVo){
        if(bidInfoVo.getActualPayBeans().compareTo(CalcUtils.ZERO) == 0
                && bidInfoVo.getMortgageFreeBean().compareTo(CalcUtils.ZERO) == 0){
            log.warn("Commission and with beans are both equal to zero,please check.");
            return Result.bidFail("出价失败");
        }
        if(isBidSuccess(bidInfoVo,CalcUtils.COMMISSION_PROPORTION)){
            //获取用户徽章信息
            List<BadgeCustomerVo> badgeCustomerInfos = getCustomerBadgeInfos(bidInfoVo.getCustomerId());
            //更新拍卖值表、排行表
            updateOrInsertAuctionValueData(bidInfoVo,badgeCustomerInfos);

            //更新标志
            int updateFlag = 0;
            //更新贡献徽章
            //用户存在贡献徽章信息，更新之，否则新增贡献徽章记录
            if(badgeCustomerInfos != null && badgeCustomerInfos.size() > 0){
                for(BadgeCustomerVo badgeCustomerInfo : badgeCustomerInfos){
                    if(badgeCustomerInfo.getName().equals("贡献徽章")){
                        updateCustomerBadgeBeans(badgeCustomerInfo.getEmblemId()
                                ,bidInfoVo.getCustomerId()
                                ,bidInfoVo.getActualPayBeans()
                                ,badgeCustomerInfo.getBeans()
                                ,2);
                        updateFlag = 1;
                        break;
                    }else{
                        log.info("Emblem type is " + badgeCustomerInfo.getName() + ",skip it.");
                    }
                }
                //list中不包含用户的贡献徽章信息，则未执行更新操作，此时需新增记录
                if(updateFlag == 0){
                    insertCustomerBadgeBeans(bidInfoVo.getCustomerId(),bidInfoVo.getActualPayBeans(),2);
                }
            }else{
                insertCustomerBadgeBeans(bidInfoVo.getCustomerId(),bidInfoVo.getActualPayBeans(),2);
            }
            //更新好友徽章
            //查询该用户被谁邀请
            Customer customer = customerMapper.selectById(bidInfoVo.getCustomerId());
            if(customer.getInvitId() != null) {
                int invitedId = customer.getInvitId();
                List<BadgeCustomerVo> badgeInvitedCustomerInfos = getCustomerBadgeInfos(invitedId);
                //邀请用户存在好友徽章，更新之，否则新增好友徽章记录
                if(badgeInvitedCustomerInfos != null && badgeInvitedCustomerInfos.size() > 0){
                    updateFlag = 0;
                    for(BadgeCustomerVo badgeInvitedCustomerInfo : badgeInvitedCustomerInfos){
                        if(badgeInvitedCustomerInfo.getName().equals("好友徽章")){
                            updateCustomerBadgeBeans(badgeInvitedCustomerInfo.getEmblemId()
                                    ,badgeInvitedCustomerInfo.getCustomerId()
                                    ,bidInfoVo.getActualPayBeans()
                                    ,badgeInvitedCustomerInfo.getBeans()
                                    ,1);
                            updateFlag = 1;
                            break;
                        }else{
                            log.info("Emblem type is " + badgeInvitedCustomerInfo.getName() + ",skip it.");
                        }
                    }

                    //list中不包含用户的贡献徽章信息，则未执行更新操作，此时需新增记录
                    if(updateFlag == 0){
                        insertCustomerBadgeBeans(invitedId,bidInfoVo.getActualPayBeans(),1);
                    }
                }else{
                    insertCustomerBadgeBeans(invitedId,bidInfoVo.getActualPayBeans(),1);
                }

            }else{
                log.info("用户" + customer.getName() + "无对应的邀请用户");
            }

            //入库加价表
            MarkupRecordSummary markupRecordSummary = markupRecordMapper.selectSummaryByGoodsId(bidInfoVo.getGoodsId());
            insertMarkupRecord(bidInfoVo,markupRecordSummary);
            //入库交易流水
            insertTransLog(bidInfoVo);
            try {
                DealConcluedVo dealConcluedVo = iDealService.isDealConclued(bidInfoVo.getGoodsId());
                //若拍中，则执行入库和结转
                if(dealConcluedVo.isConclued()){
                    //处理拍中后的入库
                    //包括订单表、拍品表、中拍记录表、消息表
                    dealWinnerStorage(bidInfoVo.getGoodsId(),dealConcluedVo.getConcluedUserId(),customer.getName());

                    //用戶返佣
                    iDealService.executeCustomerCommision(dealConcluedVo.getConcluedUserId(),bidInfoVo.getGoodsId());
                    //平台返佣
                    iDealService.executePlatformCommision(dealConcluedVo.getConcluedUserId(),bidInfoVo.getGoodsId());
                    //代理返佣
                    iDealService.executeAgentCommision(dealConcluedVo.getConcluedUserId(),bidInfoVo.getGoodsId());
                    //处理拍中后的结转
                    //包括加价表、好友助力表、拍卖值表、排行表
                    dealWinnerCarryforward(bidInfoVo.getGoodsId(),dealConcluedVo.getConcluedUserId());
                    return Result.bidSuccessConcluedSuccess("出价成功并拍中");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.bidSuccessConcluedFail("出价成功未拍中");
        }else{
            return Result.bidFail("出价失败");
        }
    }

}
