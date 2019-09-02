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
 *  App版本配置类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本标识id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 1：安卓，2：iOS
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * 版本号,iOS、Android平台
     */
    @TableField("version")
    private String version;

    /**
     * apk下载地址
     */
    @TableField("apk_url")
    private String apkUrl;

    /**
     * explanation
     */
    @TableField("explanation")
    private String explanation;

    /**
     * 广告图地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 0：普通更新，1：强制更新
     */
    @TableField("is_rape")
    private Integer isRape;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


}
