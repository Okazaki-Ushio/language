package com.yosang.language.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.annotation.NotNullProps;
import com.yosang.language.utils.JsonUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LanguageInterceptor implements HandlerInterceptor {

    private static String Message = "不能为空";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String aa = request.getParameter("aa");

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NotNullProps checkPropers = handlerMethod.getMethod().getAnnotation(NotNullProps.class);
            if (checkPropers == null) {
                return true;
            } else {
                for (String param : checkPropers.value()) {
                    String parameter = request.getParameter(param);
                    if (null == parameter||parameter.equals("")) {
                        JSONObject object = JsonUtils.fail(1001,param + Message);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json;charset=utf-8");
                        try (PrintWriter writer = response.getWriter()) {
                            writer.write(object.toJSONString());
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
