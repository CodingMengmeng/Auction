package com.example.auctionapp.wxapi.vo;

public class GoodsDetail {

    /**
     * 商品的编号(必填 32)
     */
    private String goods_id;
    /**
     * 微信支付定义的统一商品编号(可选 32)
     */
    private String wxpay_goods_id;
    /**
     * 商品名称(必填 256)
     */
    private String goods_name;
    /**
     * 商品数量(必填)
     */
    private int quantity;
    /**
     * 商品单价(必填，单位为分)
     */
    private int price;
    /**
     * 商品类目ID(可选 32)
     */
    private String goods_category;
    /**
     * 商品描述信息(可选 1000)
     */
    private String body;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getWxpay_goods_id() {
        return wxpay_goods_id;
    }

    public void setWxpay_goods_id(String wxpay_goods_id) {
        this.wxpay_goods_id = wxpay_goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getGoods_category() {
        return goods_category;
    }

    public void setGoods_category(String goods_category) {
        this.goods_category = goods_category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
