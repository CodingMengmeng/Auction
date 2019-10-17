package com.example.auctionapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 拍品
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuctionGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(exist = false)
    private Integer customerId;

    /**
     * 组织者id
     */
    @TableField("organizer_id")
    private Integer organizerId;

    /**
     * 拍品名字
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 商品分类（分类中最下一层的id）
     */
    @TableField("goods_type")
    private Integer goodsType;

    /**
     * 限时拍卖开始时间
     */
    @TableField("begin_time")
    private Date beginTime;

    /**
     * 限时拍卖结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 顺延时间
     */
    @TableField("postpone_time")
    private Integer postponeTime;

    /**
     * 拍品价格
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;

    /**
     * 利润金额
     */
    @TableField("profit")
    private BigDecimal profit;

    /**
     * 起拍价格
     */
    @TableField("starting_price")
    private BigDecimal startingPrice;

    /**
     * 单次佣金
     */
    @TableField("single_commission")
    private BigDecimal singleCommission;

    /**
     * 保证金
     */
    @TableField("bond")
    private BigDecimal bond;

    /**
     * 拍品编号
     */
    @TableField("number")
    private String number;

    /**
     * 上拍次数
     */
    @TableField("auction_num")
    private Integer auctionNum;

    /**
     * 状态1为上拍,0为未上拍,2已拍出
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注字段
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 乐观锁标志
     */
    @TableField("version")
    private Integer version;

    /**
     * 单个图片地址
     */
    @TableField(exist = false)
    private String goodsImg;

    /**
     * 轮次
     */
    @TableField("rounds")
    private Integer rounds;

    /**
     * 拍豆池
     */
    @TableField("beans_pond")
    private BigDecimal beansPond;
}
