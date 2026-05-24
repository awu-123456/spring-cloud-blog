package com.bite.common.pojo;

import com.bite.common.enums.ResultCodeEnum;
import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String errMsg;
    private T data;

    public static <T> Result success(T data){
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> Result fail(String errMsg){
        Result result = new Result();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setErrMsg(errMsg);
        return result;
    }
}
