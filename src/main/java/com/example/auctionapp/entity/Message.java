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
 * 账户
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账户类型 1:系统消息 2：用户消息
     */
    @TableField("type")
    private Integer type;

    /**
     * 拍品id
     */
    @TableField("goods_id")
    private Integer goodsId;
    /**
     * 主体id
     */
    @TableField("subject_id")
    private Integer subjectId;

    /**
     * 消息内容
     */
    @TableField("message_info")
    private String messageInfo;

    /**
     * 发送标志 0：未发送 1：已发送
     */
    @TableField("send_flag")
    private Integer sendFlag;

    /**
     * 读标志 0：未读 1：已读
     */
    @TableField("read_flag")
    private Integer readFlag;

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


}
