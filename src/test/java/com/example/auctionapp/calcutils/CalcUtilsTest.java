package com.example.auctionapp.calcutils;

import com.example.auctionapp.util.CalcUtils;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @description 
 * @author mengjia
 * @date 2019/8/29
 * @version 1.0
 * @see 
 */
public class CalcUtilsTest {
    @Test
    public void calcAuctionValueTest(){
        BigDecimal commissionValue = new BigDecimal("150");
        BigDecimal ctrbBadgeValue = new BigDecimal("1.00");
        BigDecimal friendBadgeValue = new BigDecimal("1.02");
        BigDecimal friendAsstValue = new BigDecimal("33");
        BigDecimal result = CalcUtils.calcAuctionValue(commissionValue,ctrbBadgeValue,
                friendBadgeValue,friendAsstValue);
        System.out.println(result);
    }
}
