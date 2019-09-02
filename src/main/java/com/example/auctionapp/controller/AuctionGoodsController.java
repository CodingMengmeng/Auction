package com.example.auctionapp.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.annotation.ObjectNotNull;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.entity.*;
import com.example.auctionapp.service.*;
import com.example.auctionapp.vo.AuctionGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

import static com.example.auctionapp.core.Result.ResultCode.SUCCESS;

/**
 * <p>
 * 拍品 前端控制器
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("/auctionGoods")
public class AuctionGoodsController {

    @Autowired
    private IAuctionGoodsService iAuctionGoodsService;

    @Autowired
    private IAuctionService iAuctionService;
    @Autowired
    private IRedisService iRedisService;

    @Resource
    IMarkupRecordService markupRecordService;

    @Resource
    IGoodsOrderService goodsOrderService;

    /**
     * 根据拍品id查询拍品详情
     *
     * @param auctionGoods
     * @return
     * @author 马会春
     */
    @WebLog("查询拍品详情接口")
    @PostMapping("/getGoodsDetailById")
    public Result getGoodsDetailById(@RequestBody AuctionGoods auctionGoods) {
        //log.info("查询拍品详情接口，接收参数,param --> {}",auctionGoods);
        //参数校验
        if (auctionGoods == null || auctionGoods.getId() == null) {
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        Map<String, Object> goodsDetailById = iAuctionGoodsService.getGoodsDetailById(auctionGoods);
        return ResultGenerator.genSuccessResult(goodsDetailById);
    }

    /**
     * 详情页中我的出价
     *
     * @param auctionGoods
     * @return
     * @author 马会春
     */
    @WebLog("详情页面我的出价接口")
    @PostMapping("/getMarkupRecord")
    public Result getMarkupRecord(@RequestBody AuctionGoods auctionGoods) {
        //log.info("详情页面我的出价接口，接收参数,param --> {}",auctionGoods);
        if (auctionGoods.getCustomerId() == null || auctionGoods.getId() == null) {
            return null;
        }
        List<Map<String, Object>> markupRecord1 = iAuctionGoodsService.getMarkupRecord(auctionGoods);
        log.info(markupRecord1.toString());
        return ResultGenerator.genPagingResult(new Long(markupRecord1.size()), markupRecord1);
    }

    /**
     * 客户添加或取消关注
     *
     * @param followRecord
     * @return
     * @author 马会春
     */
    @WebLog("详情页面客户添加或取消关注接口")
    @PostMapping("/addFollow")
    public Result addFollow(@RequestBody FollowRecord followRecord) {
        //log.info("详情页面客户添加或取消关注接口，接收参数,param --> {}",followRecord);
        if (followRecord == null || followRecord.getCustomerId() == null || followRecord.getGoodId() == null || followRecord.getFollowFlag() == null) {
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }
        return iAuctionGoodsService.addFollow(followRecord);
    }

    /**
     * 已拍下客户信息
     *
     * @param goodsId
     * @return
     * @auchor 马会春
     */
    @WebLog("详情页面获取已拍下客户信息接口")
    @PostMapping("/getSuccessPat")
    public Result getSuccessPat(Integer goodsId) {
        //log.info("详情页面获取已拍下客户信息接口，接收参数,param --> {}",goodsId);
        if (goodsId == null) {
            return ResultGenerator.genSuccessMEssageResult("参数错误");
        }

        return iAuctionGoodsService.getSuccessPat(goodsId);
    }


    /**
     * 拍品余额拍卖
     *
     * @param transLog
     * @return
     */
    @WebLog("拍品余额拍卖请求")
    @PostMapping("/auctionByBalance")
    public Result auctionByBalance(@RequestBody TransLog transLog) {
        //log.info("拍品余额拍卖请求：{}", transLog);
        //参数校验
        if (transLog == null ||
                transLog.getGoodsId() == null ||
                transLog.getSubject() == null ||
                transLog.getCurrentBid() == null ||
                transLog.getAmount() == null ||
                transLog.getPayAmount() == null ||
                transLog.getTransType() == null ||
                transLog.getChannel() == null
        ) {
            return ResultGenerator.genSuccessMEssageResult("参数不全，请确认");
        }
        if (!transLog.getChannel().equals(ProjectConstant.PAY_CHANNEL_BALANCE)) {
            return ResultGenerator.genSuccessMEssageResult("支付方式不正确");
        }

        return iAuctionService.auctionByBalance(transLog);
    }

    /**
     * 查询已经开始拍卖的拍品
     *
     * @param paging author 马会春
     * @return
     */
    @WebLog("首页已经开始拍卖的拍品接口")
    @RequestMapping(value = "/proceedAuctionGoods", method = RequestMethod.POST)
    public Result proceedAuctionGoods(@RequestBody Paging paging) {
        //log.info("首页已经开始拍卖的拍品接口，接收参数,param --> {}",paging);
        IPage<Map<String, Object>> mapIPage = iAuctionGoodsService.proceedAuctionGoods(paging);
        return ResultGenerator.genPagingResult(mapIPage.getTotal(), mapIPage.getRecords());
    }

    /**
     * 首页轮询
     * author 马会春
     *
     * @return
     */

    public Result polling() {
        List<String> list = new ArrayList<>();
        IPage<Map<String, Object>> polling = iAuctionGoodsService.polling();
        List<Map<String, Object>> records = polling.getRecords();
        StringBuilder str = new StringBuilder("恭喜");
        for (Map<String, Object> map : records) {
            str = str.append(map.get("name")).append("以").append("<strong style=\"color: red;\">" + map.get("actual_payment") + "元</strong>").append("拍到").append("<strong style=\"color: red;\">" + map.get("goods_name") + "</strong>!");
            list.add(str.toString());
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 首页轮播图
     * author 马会春
     *
     * @return
     */
    @WebLog("首页轮播图接口")
    @RequestMapping(value = "/slideshow", method = RequestMethod.POST)
    public Result slideshow() {
        List<Banner> slideshow = iAuctionGoodsService.slideshow();
        return ResultGenerator.genSuccessResult(slideshow);
    }


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    @WebLog("查询出用户正在参拍的商品")
    @PostMapping("selectCustomerCompete")
    public Result selectCustomerCompete(@RequestBody Page<AuctionGoods> page,
                                        @RequestHeader("userId") Integer customerId) {
        return Result.success(iAuctionGoodsService.selectCustomerCompete(page, customerId));
    }


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    @WebLog("查询出用户正在参拍的商品")
    @PostMapping("selectCustomerInCompete")
    public Result selectCustomerInCompete(@RequestBody Page<AuctionGoods> page,
                                          @NotNull @RequestHeader("userId") Integer customerId) {
        return Result.success(iAuctionGoodsService.selectCustomerInCompete(page, customerId));
    }


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    @PostMapping("selectCustomerOutCompete")
    public Result selectCustomerOutCompete(@RequestBody Page page,
                                           @RequestHeader("userId") Integer customerId) {

        return Result.success(iAuctionGoodsService.selectCustomerOutCompete(page, customerId));
    }

    /**
     * 查询出用户正在关注的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    @WebLog("查询出用户正在关注的商品")
    @PostMapping("selectCustomerAttention")
    public Page<AuctionGoods> selectCustomerAttention(Page<AuctionGoods> page,
                                                      @RequestHeader("userId") Integer customerId) {
        return iAuctionGoodsService.selectCustomerAttention(page, customerId);
    }

    /**
     * 查询出用户浏览历史的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    @WebLog("查询出用户浏览历史的商品")
    @PostMapping("selectCustomerBrowse")
    public Page<AuctionGoods> selectCustomerBrowse(Page<AuctionGoods> page,
                                                   @RequestHeader("userId") Integer customerId) {
        return iAuctionGoodsService.selectCustomerAttention(page, customerId);
    }


    /**
     * 根据类型查询出所有的商品
     *
     * @param map
     * @return
     */
    @WebLog("根据类型查询出所有的商品")
    @PostMapping("selectByType")
    public Result selectByType(@RequestBody Map map) {

        int current = Integer.parseInt(Optional.ofNullable(map.get("current")).orElse(1).toString());
        int size = Integer.parseInt(Optional.ofNullable(map.get("size")).orElse(10).toString());

        int typeId = Integer.parseInt(Optional.ofNullable(map.get("typeId")).orElse(10).toString());
        int sort = Integer.parseInt(Optional.ofNullable(map.get("sort")).orElse(10).toString());

        return Result.success(iAuctionGoodsService.selectByType(new Page<>(current, size), typeId, sort));
    }

    /**
     * 每一个小时，获取随机8个上拍的拍品，用于前端展示
     *
     * @return
     */
    @PostMapping("/selectRandomAuctionGoods")
    public Result selectRandomAuctionGoods() {
        List<Integer> list = (List<Integer>) iRedisService.get(ProjectConstant.AUCTION_GOODS_ID_RANDOM);
        log.info("捡漏接口redis中获取的拍品id-->{}", list);
        if (list == null) {
            return ResultGenerator.genSuccessResult(null);
        }
        List<Map<String, Object>> auctionGoods = iAuctionGoodsService.getRandomAuctionGoods(list);
        return ResultGenerator.genSuccessResult(auctionGoods);
    }

    /**
     * 获取最高出价和排名
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("getNowMarkupInfo")
    public Result getNowMarkupInfo(@RequestHeader("userId") Integer userId, @NotNull Integer goodsId,
                                   @NotNull BigDecimal curBid) {


        AuctionGoods goods = iAuctionGoodsService.getAuctionGoodsById(goodsId);

        Map<String, Object> nowMarkupInfo;
        BigDecimal currentBid;
        if (goods.getStatus().equals(1)) {
            nowMarkupInfo = markupRecordService.getNowMarkupInfo(userId, goodsId);
            if (nowMarkupInfo != null) {
                currentBid = new BigDecimal(nowMarkupInfo.get("currentBid").toString());
                if (curBid.compareTo(currentBid) == 0) {
                    nowMarkupInfo.put("winningFlag", "0");
                    return Result.success(nowMarkupInfo);
                }
            }
            return Result.errorInfo(SUCCESS, "未找到交易信息!");
        } else {
            nowMarkupInfo = markupRecordService.getNowMarkupHisInfo(userId, goodsId);
            if (nowMarkupInfo == null) {
                return Result.errorInfo(SUCCESS, "手慢了，宝贝被其他人抢走啦~");
            }
            GoodsOrder goodsOrder = goodsOrderService.getGoodsOrderByGoodsId(goodsId);
            if (goodsOrder == null) {
                return Result.errorInfo("系统错误!");
            }

            if (userId.equals(goodsOrder.getCustomerId())) {
                nowMarkupInfo.put("winningFlag", "1");
            } else {
                currentBid = new BigDecimal(nowMarkupInfo.get("currentBid").toString());
                if (curBid.compareTo(currentBid) != 0) {
                    return Result.errorInfo(SUCCESS, "手慢了，宝贝被其他人抢走啦~");
                }
                nowMarkupInfo.put("winningFlag", "0");
            }
            return Result.success(nowMarkupInfo);
        }

    }

}
