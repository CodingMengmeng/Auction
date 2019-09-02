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
 * 拍品类型
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodsType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自关联当前表ID
     */
    @TableField("pt_id")
    private Integer ptId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 分类级别,1:一级,2:二级
     */
    @TableField("rank")
    private Integer rank;

    /**
     * 分类图片
     */
    @TableField("type_img")
    private String typeImg;

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
