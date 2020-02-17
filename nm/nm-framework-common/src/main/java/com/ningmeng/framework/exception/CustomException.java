package com.ningmeng.framework.exception;

import com.ningmeng.framework.model.response.ResultCode;

/**
 * 自定义异常  用于程序员自己抛出使用
 * Created by 1 on 2020/2/13.
 */
public class CustomException extends RuntimeException{
    //
    private ResultCode resultCode;

    //构造方法
    public CustomException(ResultCode resultCode){
        //打印一条错误信息  错误码+异常信息
        super("错误代码："+resultCode.code()+"错误信息"+resultCode.message());
        this.resultCode = resultCode;
    }

    //取错误码  给用户返回
    public ResultCode getResultCode(){
        return resultCode;
    }
}
