package com.example.auctionapp.core;

public class Paging {
    /**
     * 页数
     */
    private Integer page=1;

    /**
     * 行数
     */
    private Integer rows=10;

    /**
     * 首页拍品搜索（分类、拍品名称）
     */
    private String name;

    /**
     * 获取拍场id
     */
    private Integer fieldId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Paging(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public Paging() {
    }
}
