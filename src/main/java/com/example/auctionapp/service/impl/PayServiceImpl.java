package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.*;
import com.example.auctionapp.service.IAuctionService;
import com.example.auctionapp.service.IGoodsOrderService;
import com.example.auctionapp.service.IPayService;
import com.example.auctionapp.service.ITransLogService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


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
public class PayServiceImpl implements IPayService{

    @Resource
    TransLogMapper transLogMapper;

    @Resource
    ITransLogService iTransLogService;

    @Resource
    IAuctionService iAuctionService;

    @Resource
    IGoodsOrderService iGoodsOrderService;

    /**
     * 三方支付回调统一处理接口
     * @param transCode
     * @param orderNumber
     * @return
     */
    @Override
    public boolean PayNotify(String transCode, String orderNumber) {
        boolean result = false;
        //获取交易信息
        TransLog  transLog = transLogMapper.selectByOrderNumber(orderNumber);
        transLog.setTransCode(transCode);
        //判断交易类型
        switch (transLog.getTransType()){
            case 1 :
                result = iTransLogService.customerRecharge(transLog);
                break;
            case 6 :
            case 7 :
                result = iAuctionService.auctionByThird(transLog);
                break;
            case 10 :
                result = iGoodsOrderService.orderPayByThird(transLog);
                break;
            default :
        }
        return result;
    }

    /**
     * 三方支付回调失败处理
     * @param transCode
     * @param orderNumber
     * @return
     */
    @Transactional
    @Override
    public void PayNotifyFailure(String transCode, String orderNumber){
        //获取交易信息
        TransLog  transLog = transLogMapper.selectByOrderNumber(orderNumber);
        transLog.setTransCode(transCode);
        //修改交易状态为失败
        transLogMapper.updateFailureByOrderNumber(transLog);
    }
}
