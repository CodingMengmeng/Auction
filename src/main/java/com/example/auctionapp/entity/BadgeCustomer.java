package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description 
 * @author mengjia
 * @date 2019/9/1
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BadgeCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 徽章等级编号
     */
    @TableId(value = "emblem_id", type = IdType.AUTO)
    private Integer emblemId;
    /**
     * 用户编号
     */
    @TableField(value="customer_id")
    private Integer customerId;
    /**
     * 记录消耗拍豆
     */
    @TableField(value="beans")
    private BigDecimal beans;

}
