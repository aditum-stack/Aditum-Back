package com.ten.aditum.back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ten.aditum.back.mapper")
public class AditumWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AditumWebApplication.class, args);
    }

}
