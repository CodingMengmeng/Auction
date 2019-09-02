package com.example.auctionapp.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 抽奖信息表
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LotteryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户主键
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 活动主键
     */
    @TableField("activity_id")
    private Integer activityId;

    /**
     * 优惠券主键
     */
    @TableField("coupons_id")
    private Integer couponsId;

    /**
     * 是否中奖 0：否，1：是
     */
    @TableField("type")
    private String type;

    /**
     * 是否已开奖 0：否，1：是
     */
    @TableField("flag")
    private String flag;

    /**
     * 数据状态 0：失效 1：生效
     */
    @TableField("status")
    private String status;

    /**
     * 生成的兑奖码
     */
    @TableField("expiry_code")
    private String expiryCode;

    /**
     * 备注
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
     * 乐观锁版本号
     */
    @Version
    private Integer version;


    @Override
    public String toString() {
        return "LotteryInfo{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", activityId=" + activityId +
                ", couponsId=" + couponsId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", expiryCode='" + expiryCode + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                '}';
    }
}
