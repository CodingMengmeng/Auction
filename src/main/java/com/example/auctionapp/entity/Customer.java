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
 * 客户
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *用户头像
     */
    @TableField("head_portrait")
    private String headPortrait;

    /**
     * 用户昵称
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 微信unionid
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 微信openId
     */
    @TableField("open_id")
    private String openId;

    /**
     * 微信授权通道 APP:app授权 WEB:网站授权 WPN:公众号
     */
    @TableField("wx_channel")
    private String wxChannel;
    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 性别 1:男,0:女
     */
    @TableField("sex")
    private String sex;

    /**
     * 代理ID
     */
    @TableField("agent_id")
    private Integer agentId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 身份证号
     */
    @TableField("identity_card")
    private String identityCard;

    /**
     * 证件类型
     */
    @TableField("identity_card_type")
    private String identityCardType;

    /**
     * 实名验证状态{0:未知名认证,1:已实名认证}
     */
    @TableField("verify_status")
    private Integer verifyStatus;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

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
