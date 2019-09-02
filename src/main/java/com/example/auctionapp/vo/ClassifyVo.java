package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 一级分类返回结果集 结果返回类
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="一级分类返回结果集", description="分类对象")
public class ClassifyVo {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String name;
}
