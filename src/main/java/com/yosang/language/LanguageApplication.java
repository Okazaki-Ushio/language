package com.yosang.language;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LanguageApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanguageApplication.class, args);
    }

}
