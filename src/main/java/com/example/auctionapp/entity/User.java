package com.example.auctionapp.entity;

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
 * 后台用户表
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 登录密码（加密）
     */
    @TableField("login_pass")
    private String loginPass;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("add_date")
    private LocalDateTime addDate;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 最近一次登录时间
     */
    @TableField("login_date")
    private LocalDateTime loginDate;

    /**
     * 状态：0为冻结；1为正常
     */
    @TableField("lockup")
    private Integer lockup;

    /**
     * 数据筛选
     */
    @TableField("val")
    private String val;

    /**
     * 外键，自关联的外键
     */
    @TableField("mid")
    private Integer mid;

    /**
     * 角色（role）id
     */
    @TableField("bumen")
    private Integer bumen;

    @TableField("text")
    private String text;

    /**
     * 用户类型: 1,管理员 2，代理 3，组织者
     */
    @TableField("type")
    private Integer type;

    @TableField("version")
    private Integer version;


}
