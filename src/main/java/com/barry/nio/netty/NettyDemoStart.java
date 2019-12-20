package com.barry.nio.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * TaskApplication [spring boot] 主方法
 *
 * 启动方式，右键->run/debug->Spring Boot App
 *
 * @author
 *
 */
@SpringBootApplication
@Slf4j
public class NettyDemoStart extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NettyDemoStart.class, args);
    }

}
