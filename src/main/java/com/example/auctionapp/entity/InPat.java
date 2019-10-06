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
 * @description in_pat表映射实体
 * @author mengjia
 * @date 2019/10/6
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InPat implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 编号，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户编号
     */
    @TableField(value = "customer_id")
    private Integer customerId;

    /**
     * 拍品编号
     */
    @TableField(value="goods_id")
    private Integer goodsId;

    /**
     * 出价金额
     */
    @TableField(value="bid_price")
    private BigDecimal bidPrice;

    /**
     * 支付拍豆
     */
    @TableField(value="consume_beans")
    private BigDecimal consumeBeans;

    /**
     * 支付赠豆
     */
    @TableField(value="consume_give")
    private BigDecimal consumeGive;

    /**
     * 拍卖值
     */
    @TableField(value="customer_value")
    private BigDecimal customerValue;

    /**
     * 好友助力
     */
    @TableField(value="friend_help")
    private BigDecimal friendHelp;

    /**
     * 创建时间
     */
    @TableField(value="create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value="update_time")
    private LocalDateTime updateTime;
}
