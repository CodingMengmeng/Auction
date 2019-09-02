package com.example.auctionapp.wxapi.process;

/**
 * OAuth token
 */
public class OAuthAccessToken extends AccessToken {

    //oauth2.0
    private String refresh_token;//刷新token
    private String openid;
    private String scope;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}

