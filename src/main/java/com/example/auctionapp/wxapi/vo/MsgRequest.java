package com.example.auctionapp.wxapi.vo;

/**
 * 用户发送给公众号的消息；
 */

public class MsgRequest {

    private String MsgType;// 消息类型
    private String MsgId;
    private String FromUserName;// openid
    private String ToUserName;
    private String CreateTime;

    // 文本消息
    private String Content;

    // 图片消息
    private String PicUrl;

    // 地理位置消息
    private String Latitude;
    private String Longitude;
    private String Scale;
    private String Label;

    // 事件消息
    private String Event;
    private String EventKey;

    // 菜单扫码推事件且弹出“消息接收中”提示框的事件
    private ScanCodeInfo ScanCodeInfo;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public ScanCodeInfo getScanCodeInfo() {
        return ScanCodeInfo;
    }

    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        ScanCodeInfo = scanCodeInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MsgRequest{");
        sb.append("MsgType='").append(MsgType).append('\'');
        sb.append(", MsgId='").append(MsgId).append('\'');
        sb.append(", FromUserName='").append(FromUserName).append('\'');
        sb.append(", ToUserName='").append(ToUserName).append('\'');
        sb.append(", CreateTime='").append(CreateTime).append('\'');
        sb.append(", Content='").append(Content).append('\'');
        sb.append(", PicUrl='").append(PicUrl).append('\'');
        sb.append(", Latitude='").append(Latitude).append('\'');
        sb.append(", Longitude='").append(Longitude).append('\'');
        sb.append(", Scale='").append(Scale).append('\'');
        sb.append(", Label='").append(Label).append('\'');
        sb.append(", Event='").append(Event).append('\'');
        sb.append(", EventKey='").append(EventKey).append('\'');
        sb.append(", ScanCodeInfo=").append(ScanCodeInfo);
        sb.append('}');
        return sb.toString();
    }
}
