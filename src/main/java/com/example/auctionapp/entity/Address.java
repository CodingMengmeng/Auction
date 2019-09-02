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
 * 地址
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地址类型 1:客户地址 2：代理地址
     */
    @TableField("type")
    private Integer type;

    /**
     * 主体ID
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 地址
     */
    @TableField("address")
    private String address;


    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 收货人
     */
    @TableField("consignee")
    private String consignee;


    /**
     * 默认标志 0:非默认 1:默认
     */
    @TableField("default_flag")
    private Integer defaultFlag;

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
