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
 * 代理
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 邀请码
     */
    @TableField("invitation_code")
    private Integer invitationCode;


    /**
     * 返佣比例类型
     */
    @TableField("proportion_type")
    private Integer proportionType;


    /**
     * 固定返佣比例
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 返佣比例
     */
    @TableField("parent_proportion")
    private BigDecimal parentProportion;

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


}
