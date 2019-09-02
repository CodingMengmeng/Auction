package com.example.auctionapp.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动信息表
 * </p>
 *
 * @author 朱秋友
 * @since 2019-06-03
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActivityInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动开启标记
     */
    public static final String activityFlag1 = "1";
    public static final String activityFlag0 = "0";


    /**
     * 营销活动类型 1： 抽奖 ， 2：其他（未定义）
     */
    public static final String activityType1 = "1";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;

    /**
     * 活动名称
     */
    @TableField("activity_code")
    private String activityCode;

    /**
     * 活动名称
     */
    @TableField("activity_type")
    private String activityType;

    /**
     * 活动是否开启 0：否，1：是
     */
    @TableField("activity_flag")
    private String activityFlag;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 活动可中奖数量
     */
    @TableField("lottery_num")
    private Integer lotteryNum;

    /**
     * 活动URL
     */
    @TableField("url")
    private String url;


    /**
     * 数据状态 0：失效 1：生效
     */
    @TableField("status")
    private String status;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private LocalDateTime currDateTime;

    @Override
    public String toString() {
        return "ActivityInfo{" +
                "id=" + id +
                ", activityName='" + activityName + '\'' +
                ", activityFlag='" + activityFlag + '\'' +
                ", startTime=" + startTime +
                ", expireTime=" + expireTime +
                ", lotteryNum=" + lotteryNum +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", currDateTime=" + currDateTime +
                ", version=" + version +
                '}';
    }
}
