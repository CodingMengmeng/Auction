package com.example.auctionapp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 徽章等级返回结果集 结果返回类
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="徽章等级返回返回结果集", description="徽章对象")
public class BadgeLevelVo {

    @ApiModelProperty(value = "客户编号")
    private Integer customerId;

    @ApiModelProperty(value = "徽章等级")
    private Integer level;

    @ApiModelProperty(value = "徽章图片路径")
    private String emblemUrl;

    @ApiModelProperty(value = "徽章名称")
    private String name;
}
