package com.lhy.pro.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2019-09-06 所属公司：lhy
 */
@Component
@Slf4j
@Order(value = 1)
public class TaskInitConfig implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("容器启动成功！");

    }
}
