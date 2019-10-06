package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description share_history表映射实体
 * @author mengjia
 * @date 2019/10/6
 * @version 1.0
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShareHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户编号
     */
    @TableField("customer_id")
    private String customerId;

    /**
     * 拍品编号
     */
    @TableField("goods_id")
    private Integer goodsId;

    /**
     * 帮助助力客户编号
     */
    @TableField("by_customers_id")
    private Integer byCustomersId;

    /**
     * 助力值
     */
    @TableField("help_value")
    private BigDecimal helpValue;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
