package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 拍品
 * </p>
 *
 * @author nqh
 * @since 2019-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuctionGoodsDealInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 成交最小区间
     */
    @TableField("min_section")
    private BigDecimal minSection;

    /**
     * 最低参拍人次
     */
    @TableField("min_number_people")
    private Integer minNumberPeople;

    /**
     * 利润金额
     */
    @TableField("profit")
    private BigDecimal profit;

    /**
     * 成本
     */
    @TableField("cost")
    private BigDecimal cost;

    /**
     * 总拍豆
     */
    @TableField("beans_pond")
    private BigDecimal beansPond;


}
