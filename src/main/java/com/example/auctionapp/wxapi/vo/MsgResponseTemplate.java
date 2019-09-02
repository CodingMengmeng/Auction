package com.example.auctionapp.wxapi.vo;

import java.util.Map;

public class MsgResponseTemplate {
    private String touser;
    private String template_id;
    private String url;
    private Map<String, BaseData> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, BaseData> getData() {
        return data;
    }

    public void setData(Map<String, BaseData> data) {
        this.data = data;
    }

    public MsgResponseTemplate(String touser, String template_id, String url, Map<String, BaseData> data) {
        super();
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.data = data;
    }

    public MsgResponseTemplate() {

    }
}
