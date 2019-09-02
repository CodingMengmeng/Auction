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
 * 账户
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账户类型 1:客户账户 2:代理商账户 3:组织者账户
     */
    @TableField("type")
    private Integer type;

    /**
     * 账户号
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 支付密码
     */
    @TableField("password")
    private String password;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    @TableField("frozen")
    private BigDecimal frozen;

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

    /**
     * 赠豆
     */
}
