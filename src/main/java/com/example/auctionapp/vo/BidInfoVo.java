package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description 拍卖出价接口请求参数Bean
 * @author mengjia
 * @date 2019/9/5
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="拍卖出价接口请求参数集",description="拍卖出价接口请求参数对象")
public class Vo {
    @ApiModelProperty(value="拍品编号")
    private Integer goodsId;

    @ApiModelProperty(value="出价用户编号")
    private Integer customerId;

    @ApiModelProperty(value="出价价格")
    private BigDecimal bidPrice;

    @ApiModelProperty(value="实际支付拍豆")
    private BigDecimal actualPayBeans;

    @ApiModelProperty(value="应支付拍豆")
    private BigDecimal shouldPayBeans;

    @ApiModelProperty(value="合计拍卖值")
    private BigDecimal totalAuctionValue;

    @ApiModelProperty(value="抵扣赠豆")
    private BigDecimal mortgageFreeBean;
}
