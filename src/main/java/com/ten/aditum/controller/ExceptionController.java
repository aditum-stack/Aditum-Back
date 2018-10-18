package com.ten.aditum.controller;

import com.ten.dto.ResponseResult;
import com.ten.exception.RequestException;
import com.ten.exception.impl.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Exception Controller
 *
 * @author Administrator
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * request请求出错
     * <p>
     * code :400
     */
    @ResponseBody
    @ExceptionHandler({
            RequestDataNullException.class,
            RequestDataFormatException.class,
            NecessaryParameterIsNullException.class})
    public ResponseResult requestError(RequestException e) {
        return new ResponseResult(400, e.getErrorMsg());
    }

    /**
     * resource is null
     * <p>
     * code :404
     */
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseResult resourceNotFoundError(ResourceNotFoundException e) {
        return new ResponseResult(404, e.getErrorMsg());
    }

    /**
     * 增加、修改、删除失败 conflict
     * <p>
     * code:409
     */
    @ResponseBody
    @ExceptionHandler(ResourceExecuteException.class)
    public ResponseResult resourceExecuteError(ResourceExecuteException e) {
        return new ResponseResult(409, e.getErrorMsg());
    }

}
