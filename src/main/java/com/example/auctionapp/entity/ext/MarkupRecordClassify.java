package com.example.auctionapp.entity.ext;

import com.example.auctionapp.entity.MarkupRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description 加价表汇总实体bean
 * @author mengjia
 * @date 2019/10/6
 * @version 1.0
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MarkupRecordClassify extends MarkupRecord {

    private static final long serialVersionUID = 1L;


    /**
     * 最高出价
     */
    private BigDecimal maxCurrentBid;

    /**
     * 参拍总次数
     */
    private Integer maxTotalNumber;

    /**
     * 总佣金-消耗拍豆
     */
    private BigDecimal sumCommission;

    /**
     * 用户参拍总次数
     */
    private Integer maxShootingTimes;

    /**
     * 总赠豆
     */
    private BigDecimal sumWithBeans;

    /**
     * 总拍豆：总消耗拍豆 + 总赠豆
     */
    private BigDecimal sumBeans;
}
