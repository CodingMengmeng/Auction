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
 * @description auction_value表映射实体
 * @author mengjia
 * @date 2019/9/4
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuctionValue implements Serializable {

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
     * 当前拍品总拍卖值
     */
    @TableField(value="customer_value")
    private BigDecimal customerValue;

    /**
     * 好友累计助力拍卖值
     */
    @TableField(value="friend_value")
    private BigDecimal friendValue;

    /**
     * 贡献徽章加成拍卖值
     */
    @TableField(value="contribute_badge_value")
    private BigDecimal contributeBadgeValue;

    /**
     * 好友徽章加成拍卖值
     */
    @TableField(value="friend_badge_value")
    private BigDecimal friendBadgeValue;

    /**
     * 最高出价
     */
    @TableField(value="bid")
    private BigDecimal bid;

    /**
     * 参拍次数
     */
    @TableField(value="num")
    private Integer num;

    /**
     * 消耗总拍豆
     */
    @TableField(value="consume_beans")
    private BigDecimal consumeBeans;

    /**
     * 消耗总赠豆
     */
    @TableField(value="consume_give")
    private BigDecimal consumeGive;

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

    /**
     * 乐观锁标志
     */
    @TableField("version")
    private Integer version;

}
