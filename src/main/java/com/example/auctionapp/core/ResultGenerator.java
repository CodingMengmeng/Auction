package com.example.auctionapp.core;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(Result.ResultCode.SUCCESS.code())
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result()
                .setCode(Result.ResultCode.SUCCESS.code())
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genSuccessMEssageResult(String message) {
        return genResult(message, Result.ResultCode.SUCCESS);
    }

    public static Result genSuccessResult(String message, Result.ResultCode resultCode) {
        return genResult(message, resultCode);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(Result.ResultCode.FAIL.code())
                .setMessage(message);
    }

    public static Result genFailResult(String message, Result.ResultCode resultCode) {
        return genResult(message, resultCode);
    }

    public static Result genResult(String message, Result.ResultCode resultCode) {
        return new Result()
                .setCode(resultCode.code())
                .setMessage(message);
    }

    public static <T> Result<T> genPagingResult(Long total, T data) {
        return new Result(total,data).setCode(Result.ResultCode.SUCCESS.code()).setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

}
