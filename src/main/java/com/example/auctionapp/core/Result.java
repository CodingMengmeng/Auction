package com.example.auctionapp.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 统一API响应结果封装
 * @author 安能
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode()
public class Result<T> {

    private Long total;
    private int code;
    private String message;
    private T data;

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String BID_FAIL_MESSAGE = "BID_FAIL";
    private static final String BID_SUCCESS_CONCLUED_FAIL_MESSAGE = "BID_SUCCESS_CONCLUED_FAIL";
    private static final String BID_SUCCESS_CONCLUED_SUCCESS_MESSAGE = "BID_SUCCESS_CONCLUED_SUCCESS";

    /**
     * 响应码枚举，参考HTTP状态码的语义
     */
    public enum ResultCode {
        
        /**
         * 成功
         */
        SUCCESS(200),
        
        /**
         * 失败
         */
        FAIL(400),
        /**
         * 未认证（签名错误）
         */
        UNAUTHORIZED(401),
        /**
         * 接口不存在
         */
        NOT_FOUND(404),
        /**
         * 服务器内部错误
         */
        INTERNAL_SERVER_ERROR(500),
        /**
         * 下单委托成功
         */
        TRADE_ORDER_INSERT_SUCCESS(6000),
        /**
         * 下单委托失败
         */
        TRADE_ORDER_INSERT_ERROR(6001),

        /**
         * 出价失败
         */
        BID_FAIL(7001),

        /**
         * 出价成功未拍中
         */
        BID_SUCCESS_CONCLUED_FAIL(7002),

        /**
         * 出价成功拍中
         */
        BID_SUCCESS_CONCLUED_SUCCESS(7003);

        private final int code;

        ResultCode(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }
    }


    public Result() {
    }

    public Result(Long total, T data) {
        this.total = total;
        this.data = data;
    }

    public static <D> Result isSuccess(boolean bool) {
        if (bool) {
            return new Result().setCode(ResultCode.SUCCESS.code).setMessage(DEFAULT_SUCCESS_MESSAGE);
        } else {
            return new Result().setCode(ResultCode.FAIL.code).setMessage(DEFAULT_SUCCESS_MESSAGE);
        }
    }


    public static <D> Result success() {
        return new Result<D>().setCode(ResultCode.SUCCESS.code).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(null);
    }

    public static <D> Result success(D date) {
        return new Result<D>().setCode(ResultCode.SUCCESS.code).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(date);
    }

    public static <D> Result errorInfo() {
        return errorInfo(ResultCode.FAIL.code, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static <D> Result errorInfo(String message) {
        return errorInfo(ResultCode.FAIL.code, message, null);
    }

    public static <D> Result errorInfo(ResultCode code, String message) {
        return errorInfo(code.code, message, null);
    }

    public static <D> Result errorInfo(int  code, String message, D data) {
        return new Result<D>().setCode(code).setMessage(message).setData(data);
    }

    public static <D> Result bidFail(D data) {
        return new Result<D>().setCode(ResultCode.BID_FAIL.code()).setMessage(BID_FAIL_MESSAGE).setData(data);
    }
    public static <D> Result bidSuccessConcluedFail(D data) {
        return new Result<D>().setCode(ResultCode.BID_SUCCESS_CONCLUED_FAIL.code()).setMessage(BID_SUCCESS_CONCLUED_FAIL_MESSAGE).setData(data);
    }
    public static <D> Result bidSuccessConcluedSuccess(D data) {
        return new Result<D>().setCode(ResultCode.BID_SUCCESS_CONCLUED_SUCCESS.code()).setMessage(BID_SUCCESS_CONCLUED_SUCCESS_MESSAGE).setData(data);
    }
}
