package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 优惠券
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Coupons implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠券名
     */
    @TableField("name")
    private String name;

    /**
     * 优惠券类型{1:权利,2:折扣,3:现金}
     */
    @TableField("coupons_type")
    private Integer couponsType;

    /**
     * 优惠内容
     */
    @TableField("coupons_info")
    private String couponsInfo;

    /**
     * 优惠券使用条件
     */
    @TableField("coupons_condition")
    private Integer couponsCondition;

    /**
     * 发放类型{1:新用户注册,2:客户邀请好友,3:被客户邀请,4拍卖}
     */
    @TableField("send_type")
    private Integer sendType;

    /**
     * 类型 1-截止有效日期  2-有效周期
     */
    @TableField("expire_type")
    private Integer expireType;

    /**
     * type为1时有效
     */
    @TableField("expire_date")
    private Date expireDate;

    /**
     * type为2时有效
     */
    @TableField("expire_cycle")
    private Integer expireCycle;

    /**
     * 开始日期
     */
    @TableField("start_date")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private Date endDate;

    /**
     * 账户状态 0：失效 1：生效
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


}
