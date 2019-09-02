package com.example.auctionapp.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 加价历史
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MarkupRecordHis implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 佣金状态 0:未支付 1:已支付 2:已退回
     */
    @TableField("cms_status")
    private Integer cmsStatus;

    /**
     * 卖场id
     */
    @TableField("field_id")
    private Integer fieldId;

    /**
     * 商品Id
     */
    @TableField("goods_id")
    private Integer goodsId;

    /**
     * 当前出价
     */
    @TableField("current_bid")
    private BigDecimal currentBid;

    /**
     * 参拍总次数
     */
    @TableField("total_number")
    private Integer totalNumber;

    /**
     * 用户参拍次数
     */
    @TableField("shooting_times")
    private Integer shootingTimes;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 定金金额/保证金
     */
    @TableField("bond")
    private BigDecimal bond;

    /**
     * 佣金
     */
    @TableField("commission")
    private BigDecimal commission;

    /**
     * 优惠金额
     */
    @TableField("coupons")
    private BigDecimal coupons;

    /**
     * 优惠类型
     */
    @TableField("coupons_type")
    private Integer couponsType;

    /**
     * 实际支付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 支付方式
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注字段
     */
    @TableField("remark")
    private String remark;

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

    /**
     * 乐观锁标志
     */
    @TableField("version")
    private Integer version;

}
