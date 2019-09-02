package com.example.auctionapp.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@Order(1)
//@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String handle(Exception e) {

        logger.error("【系统异常】{}", e);
        return e.getMessage();
    }
}
