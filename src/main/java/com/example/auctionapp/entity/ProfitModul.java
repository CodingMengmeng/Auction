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
 * 分润模板
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfitModul implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地址类型 1:客户地址 2：代理地址
     */
    @TableField("start_price")
    private BigDecimal startPrice;

    /**
     * 主体ID
     */
    @TableField("end_price")
    private BigDecimal endPrice;

    /**
     * 返佣比例
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 父及代理分润比例
     */
    @TableField("parent_proportion")
    private BigDecimal parentProportion;

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
