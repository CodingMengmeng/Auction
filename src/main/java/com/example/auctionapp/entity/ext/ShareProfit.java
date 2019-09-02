package com.example.auctionapp.entity.ext;

import com.example.auctionapp.entity.Agent;

import java.math.BigDecimal;

public class ShareProfit extends Agent {

    /**
     * 分润总佣金
     */
    private BigDecimal sumCommission;

    public BigDecimal getSumCommission() {
        return sumCommission;
    }

    public void setSumCommission(BigDecimal sumCommission) {
        this.sumCommission = sumCommission;
    }
}
