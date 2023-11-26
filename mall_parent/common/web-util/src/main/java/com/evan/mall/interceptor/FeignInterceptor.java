package com.evan.mall.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class FeignInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        //  微服务远程调用使用feign ，feign 传递数据的时候，没有。
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        //  添加header 数据
        if (request != null) {
            requestTemplate.header("userTempId", request.getHeader("userTempId"));
        }
        if (request != null) {
            requestTemplate.header("userId", request.getHeader("userId"));
        }
    }
}
