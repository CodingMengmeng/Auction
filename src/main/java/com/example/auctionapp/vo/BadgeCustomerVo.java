package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description 用户徽章信息返回结果集
 * @author mengjia
 * @date 2019/9/1
 * @version 1.0
 * @see 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="用户徽章信息返回结果集",description="用户徽章信息对象")
public class BadgeCustomerVo {
    @ApiModelProperty(value="客户编号")
    private Integer customerId;

    @ApiModelProperty(value="徽章等级编号")
    private Integer emblemId;

    @ApiModelProperty(value="徽章编号")
    private Integer id;

    @ApiModelProperty(value="徽章等级")
    private Integer level;

    @ApiModelProperty(value="徽章名称")
    private String name;

    @ApiModelProperty(value="拍豆消耗值")
    private BigDecimal beans;

    @ApiModelProperty(value="新徽章等级编号")
    private Integer newEmblemId;
}
