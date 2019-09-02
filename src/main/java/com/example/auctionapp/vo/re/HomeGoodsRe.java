package com.example.auctionapp.vo.re;

import com.example.auctionapp.util.ResultPageRows;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询拍品接收参数
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="查询拍品接收参数", description="拍品对象")
public class HomeGoodsRe extends ResultPageRows {

    @ApiModelProperty(value = "分类Id")
    private Integer parentId;

    @ApiModelProperty(value = "拍品名称")
    private String name;
}
