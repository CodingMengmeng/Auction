package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 查询拍品结果集 结果返回类
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="查询拍品结果集", description="分类对象")
public class HomeGoodsVo {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "图片地址")
    private String url;

    @ApiModelProperty(value = "拍品名称")
    private String goodsName;

    @ApiModelProperty(value = "拍品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startingPrice;
}
