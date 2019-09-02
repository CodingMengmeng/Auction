package com.example.auctionapp.entity;

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
 * 银行卡
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 账户类型 1-客户 2-代理 3商户
     */
    @TableField("acct_type")
    private Integer acctType;

    /**
     * 银行卡姓名
     */
    @TableField("acct_name")
    private String acctName;

    /**
     * 预留手机号
     */
    @TableField("bank_phone")
    private String bankPhone;

    /**
     * 银行名
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField("bank_card_no")
    private String bankCardNo;

    /**
     * 状态:{0:不生效,1:生效}
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
