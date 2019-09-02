package com.example.auctionapp.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 拍品订单
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 订单编号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 交易号
     */
    @TableField("transaction_number")
    private String transactionNumber;

    /**
     * 支付时间
     */
    @TableField("payment_time")
    private Date paymentTime;

    /**
     * 拍品id
     */
    @TableField("goods_id")
    private Integer goodsId;

    /**
     * 定金金额/保证金
     */
    @TableField("bond")
    private BigDecimal bond;

    /**
     * 定金支付渠道
     */
    @TableField("bond_channel")
    private String bondChannel;

    /**
     * 参拍次数
     */
    @TableField("total_number")
    private Integer totalNumber;

    /**
     * 总参拍次数
     */
    @TableField("total_number_all")
    private Integer totalNumberAll;

    /**
     * 总参拍佣金
     */
    @TableField("reference_commission_all")
    private BigDecimal referenceCommissionAll;

    /**
     * 参拍佣金
     */
    @TableField("reference_commission")
    private BigDecimal referenceCommission;

    /**
     * 优惠金额
     */
    @TableField("preferential_amount")
    private BigDecimal preferentialAmount;

    /**
     * 实际出价
     */
    @TableField("actual_payment")
    private BigDecimal actualPayment;

    /**
     * 尾款金额
     */
    @TableField("tail_amount")
    private BigDecimal tailAmount;

    /**
     * 尾款支付渠道
     */
    @TableField("tail_amount_channel")
    private String tailAmountChannel;

    /**
     * 物流信息
     */
    @TableField("logistics_info")
    private String logisticsInfo;

    /**
     * 订单地址id
     */
    @TableField("address_id")
    private Integer addressId;

    /**
     * 状态 0:待支付,1已支付,2以违约,3待发货,4待收货,5已完成,6待售后
     */
    @TableField("status")
    private Integer status;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 乐观锁标志
     */
    @TableField("version")
    private Integer version;

    @Override
    public String toString() {
        return "GoodsOrder{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderNumber='" + orderNumber + '\'' +
                ", transactionNumber='" + transactionNumber + '\'' +
                ", paymentTime=" + paymentTime +
                ", goodsId=" + goodsId +
                ", bond=" + bond +
                ", bondChannel='" + bondChannel + '\'' +
                ", totalNumber=" + totalNumber +
                ", totalNumberAll=" + totalNumberAll +
                ", referenceCommissionAll=" + referenceCommissionAll +
                ", referenceCommission=" + referenceCommission +
                ", preferentialAmount=" + preferentialAmount +
                ", actualPayment=" + actualPayment +
                ", tailAmount=" + tailAmount +
                ", tailAmountChannel='" + tailAmountChannel + '\'' +
                ", logisticsInfo='" + logisticsInfo + '\'' +
                ", addressId=" + addressId +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                '}';
    }
}
