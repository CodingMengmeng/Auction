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
import java.util.Date;

/**
 * <p>
 * 订单,红包,优惠券关联表
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠id
     */
    @TableField("coupon_id")
    private Integer couponId;

    /**
     * 类型{1:优惠券,2:红包}
     */
    @TableField("type")
    private Integer type;

    /**
     * 主体id
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 主体类型
     */
    @TableField("subject_type")
    private Integer subjectType;
    /**
     * 到期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 状态:{0:待使用,1:已使用,2:已过期}
     */
    @TableField("status")
    private int status;

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


    /*******业务类型字段********/

    /**
     * 优惠券发放类型
     */
    @TableField(exist = false)
    private Integer sendType;


    /**
     * 优惠券名称
     */
    @TableField(exist = false)
    private String couponsInfo;

    /**
     * 优惠券发放类型描述
     */
    @TableField(exist = false)
    private String sendTypeStr;

    /**
     * 获取优惠券的用户昵称
     */
    @TableField(exist = false)
    private String userName;
}
