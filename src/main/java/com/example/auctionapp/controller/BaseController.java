package com.example.auctionapp.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


import java.beans.PropertyEditorSupport;

/**
 * Created by Haidy on 2018/9/23
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 初始化数据绑定
     * 将所有传递进来的String进行HTML编码，防止XSS攻击
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
    }
}
