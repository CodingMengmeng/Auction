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
 * 抽奖结果表
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LotteryResult implements Serializable {

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
     * 抽奖主键
     */
    @TableField("lottery_id")
    private Integer lotteryId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否处理 0：否，1：是
     */
    @TableField("result_type")
    private String resultType;

    /**
     * 数据状态 0：失效 1：生效
     */
    @TableField("status")
    private String status;

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

    /***==========业务字段=================*/
    @TableField(exist = false)
    private String userName;  //用户的昵称

    @TableField(exist = false)
    private String headPortrait;  //用户头像

    @TableField(exist = false)
    private String expiryCode; //中奖码

    @Override
    public String toString() {
        return "LotteryResult{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", activityId=" + activityId +
                ", lotteryId=" + lotteryId +
                ", remark='" + remark + '\'' +
                ", resultType='" + resultType + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                '}';
    }
}