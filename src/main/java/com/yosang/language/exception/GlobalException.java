package com.yosang.language.exception;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JSONObject handle(Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return JsonUtils.fail(1001, "出现异常");
    }

}
