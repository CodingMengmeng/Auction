package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 徽章等级返回结果集 结果返回类
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="徽章页面结果集", description="徽章对象")
public class BadgeValueVo {

    @ApiModelProperty(value = "当前等级区间最小值")
    private Integer sectionMin;

    @ApiModelProperty(value = "当前等级区间最大值")
    private Integer sectionMax;

    @ApiModelProperty(value = "消耗拍豆--总拍卖值")
    private BigDecimal beans;

    @ApiModelProperty(value = "徽章名称")
    private String name;

    @ApiModelProperty(value = "徽章等级")
    private Integer level;

    @ApiModelProperty(value = "当前徽章等级图片")
    private String emblemUrl;

    @ApiModelProperty(value = "下一级徽章等级图片")
    private String emblemUrlNext;
}
