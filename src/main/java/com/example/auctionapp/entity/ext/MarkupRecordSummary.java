package com.example.auctionapp.entity.ext;

import com.example.auctionapp.entity.MarkupRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 加价
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MarkupRecordSummary extends MarkupRecord {

    private static final long serialVersionUID = 1L;


    /**
     * 最高出价出价
     */
    private BigDecimal maxCurrentBid;

    /**
     * 参拍总次数
     */
    private Integer maxTotalNumber;


    /**
     * 总定金金额/保证金
     */
    private BigDecimal sumBond;

    /**
     * 总佣金
     */
    private BigDecimal sumCommission;

    /**
     * 总优惠金额
     */
    private BigDecimal sumCoupons;

    /**
     * 用户参拍总次数
     */
    private Integer maxShootingTimes;

    public BigDecimal getMaxCurrentBid() {
        return maxCurrentBid;
    }

    public Integer getMaxTotalNumber() {
        return maxTotalNumber;
    }

    public BigDecimal getSumBond() {
        return sumBond;
    }

    public BigDecimal getSumCommission() {
        return sumCommission;
    }

    public BigDecimal getSumCoupons() {
        return sumCoupons;
    }

    public void setMaxCurrentBid(BigDecimal maxCurrentBid) {
        this.maxCurrentBid = maxCurrentBid;
    }

    public void setMaxTotalNumber(Integer maxTotalNumber) {
        this.maxTotalNumber = maxTotalNumber;
    }

    public void setSumBond(BigDecimal sumBond) {
        this.sumBond = sumBond;
    }

    public void setSumCommission(BigDecimal sumCommission) {
        this.sumCommission = sumCommission;
    }

    public void setSumCoupons(BigDecimal sumCoupons) {
        this.sumCoupons = sumCoupons;
    }

    public Integer getMaxShootingTimes() {
        return maxShootingTimes;
    }

    public void setMaxShootingTimes(Integer sumShootingTimes) {
        this.maxShootingTimes = sumShootingTimes;
    }
}
