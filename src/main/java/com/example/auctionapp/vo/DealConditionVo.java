package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="拍品表返回结果集",description="拍品信息")
public class DealConditionVo {
    @ApiModelProperty(value="成交最小区间")
    private BigDecimal minSection;

    @ApiModelProperty(value="最低参拍人次")
    private Integer minPeopleNum;

    @ApiModelProperty(value="利润")
    private BigDecimal profit;

    @ApiModelProperty(value="成本")
    private BigDecimal cost;

    @ApiModelProperty(value="总排豆")
    private BigDecimal beans_pond;

}
