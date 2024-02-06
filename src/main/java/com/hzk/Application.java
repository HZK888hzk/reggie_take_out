package com.hzk;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@MapperScan(value = "com.hzk.mapper")
@EnableTransactionManagement
@EnableCaching       //springcahe的redis缓存
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("项目已经启动");
    }
}