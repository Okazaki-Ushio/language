package com.yosang.language.config;

import com.yosang.language.interceptor.LanguageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LanguageInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new ActivityInterceptor()).addPathPatterns("/activity/**");
                /*.excludePathPatterns("/xx/**")*/ //exclude the static resource
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /*registry.addViewController("/").setViewName("index.html");
        registry.addViewController("/assert").setViewName("index.html");*/

    }
}
