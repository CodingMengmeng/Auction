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
 * 交易
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TransLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 卖场id
     */
    @TableField("field_id")
    private Integer fieldId;

    /**
     * 商品id
     */
    @TableField("goods_id")
    private Integer goodsId;

    /**
     * 交易发起者
     */
    @TableField("subject")
    private Integer subject;

    /**
     * 账户类型
     */
    @TableField("account_type")
    private Integer accountType;

    /**
     * 银行卡id
     */
    @TableField("bank_id")
    private Integer bankId;

    /**
     * 银行卡名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行卡卡号
     */
    @TableField("bank_card_no")
    private String bankCardNo;

    /**
     * 支付标记:{0:支出,1:收入}
     */
    @TableField("trans_sign")
    private Integer transSign;

    /**
     * 账户号
     */
    @TableField("account_id")
    private Integer accountId;

    /**
     * 交易通道
     */
    @TableField("channel")
    private String channel;

    /**
     * 支付类型 app-APP支付  h5-H5支付   mp-公众号支付
     */
    @TableField("child_channel")
    private String childChannel;

    /**
     * 交易编号(第三方支付交易编号)
     */
    @TableField("trans_code")
    private String transCode;

    /**
     * 三方支付状态 0-待支付 1-支付成功 2-支付失败 3-待退款 4-已退款 5-退款中
     */
    @TableField("trans_status")
    private Integer transStatus;

    /**
     * 交易类型 1-充值  2-提现 3-消费保证金 4-返还保证金 5-支付佣金 6-首次拍卖 7-再次拍卖 8-佣金分润 9-利润分成  
     */
    @TableField("trans_type")
    private Integer transType;

    /**
     * 当前出价
     */
    @TableField("current_bid")
    private BigDecimal currentBid;

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
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 手续费
     */
    @TableField("fee")
    private BigDecimal fee;

    /**
     * 优惠id
     */
    @TableField("coupons_id")
    private Integer couponsId;

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
     * 订单地址id
     */
    @TableField("address_id")
    private Integer addressId;

    /**
     * 交易状态：0，未审核，1，成功 2，失败
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

    @TableField(exist = false)
    private String password;


}
