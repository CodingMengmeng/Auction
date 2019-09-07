package com.example.auctionapp.bid;

import com.example.auctionapp.entity.AuctionValue;
import com.example.auctionapp.vo.BidInfoVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * @description 
 * @author mengjia
 * @date 2019/9/1
 * @version 1.0
 * @see 
 */

public class AuctionBidTest {
    @Test
    public void copyBeanTest() {
        AuctionValue vo = new AuctionValue();
        vo.setId(2);
        vo.setCustomerId(2);
        vo.setBid(new BigDecimal("2000.00"));
        vo.setCustomerValue(new BigDecimal("100.00"));
        System.out.println(vo.toString());
        AuctionValue po = new AuctionValue();
        BeanUtils.copyProperties(vo,po);
        System.out.println(po.toString());


    }
}
