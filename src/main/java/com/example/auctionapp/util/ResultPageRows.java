package com.example.auctionapp.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="分页", description="分页对象")
public class ResultPageRows {
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "每页行数")
    private Integer rows;
}
