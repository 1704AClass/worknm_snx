package com.ningmeng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 1 on 2020/2/13.
 */
//控制器增强类  类似于AOP通知
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //Throwable  异常总类  分支
    // -error（重大异常，内存溢出，内存泄漏，电脑死机）
    //-exception(程序员关注的异常) -检查时异常 -运行时异常
    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点就是一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> immutableMap;
    // 需要构建builder对象  使用buider来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    static {
        //以后在这维护错误类型
        builder.put(HttpMessageNotReadableException.class,CommonCode.HTTP_ERROR);
    }

    //捕获非自定义异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        //捕获异常
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);

        if(immutableMap == null){
            //构建Map集合
            immutableMap = builder.build();
        }
        final ResultCode resultCode = immutableMap.get(e.getClass());
        final ResponseResult responseResult;
        if(resultCode != null){
            responseResult = new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    //异常控制器   如果捕获到异常  触发下面方法
    @ExceptionHandler
    //返回前台解析
    @ResponseBody
    //自定义异常
    public ResponseResult customException(CustomException e){
        //捕获异常
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);

        return responseResult;
    }


}
