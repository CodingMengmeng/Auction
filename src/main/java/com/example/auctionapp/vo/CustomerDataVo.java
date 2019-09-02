package com.example.auctionapp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户资料返回结果集 结果返回类
 * @author MaHC
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="客户资料返回返回结果集", description="客户对象")
public class CustomerDataVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "头像")
    private String headPortrait;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "代理id")
    private Integer agentId;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "描述-备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "乐观锁")
    private String version;

    @ApiModelProperty(value = "认证状态 1-已认证  2-未认证")
    private String verifyStatus;

    @ApiModelProperty(value = "身份证号")
    private String identityCard;

    @ApiModelProperty(value = "身份证号类型")
    private String identityCardType;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "余额拍豆")
    private BigDecimal balance;

    @ApiModelProperty(value = "冻结拍豆")
    private BigDecimal frozen;

    @ApiModelProperty(value = "余额赠豆")
    private BigDecimal withBeans;

    @ApiModelProperty(value = "徽章json类")
    private List<BadgeLevelVo> badgeLevelVos;

}
