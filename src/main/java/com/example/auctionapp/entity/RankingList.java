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
 * @description ranking_list表实体
 * @author mengjia
 * @date 2019/9/30
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RankingList implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * rank 排名
     */
    @TableField("rank")
    private Integer rank;

    /**
     * customer_id，客户id
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * goods_id，拍品id
     */
    @TableField("goods_id")
    private Integer goodsId;

    /**
     * goods_value 拍卖值
     */
    @TableField("goods_value")
    private BigDecimal goodsValue;

    /**
     * create_time 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * update_time 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * version 乐观锁
     */
    @TableField("version")
    private Integer version;

    /**
     * win_rate 拍中几率
     */
    @TableField("win_rate")
    private BigDecimal winRate;

}
