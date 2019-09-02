package com.example.auctionapp.vo;

import com.example.auctionapp.entity.AuctionGoods;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 拍品 VO类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuctionGoodsVO extends AuctionGoods {


    /**
     * 浏览总和
     */
    private Integer browseNumber;

    /**
     * 排名
     */
    private Integer rank;

    private String customerName;

    private String phone;

    private String orderRemark;

    private Integer orderCustomerId;

    private String dealTime;
}
