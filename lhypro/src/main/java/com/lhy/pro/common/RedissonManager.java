package com.lhy.pro.common;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/4/11 所属公司：lhy
 */
@Configuration
public class RedissonManager {

    @Value("${redisson.address}")
    private String addressUrl;

    @Bean
    public RedissonClient getRedisson() throws Exception{
        RedissonClient redisson ;
        Config config = new Config();
        config.useSingleServer()
                .setAddress(addressUrl);
        redisson = Redisson.create(config);

        System.out.println(redisson.getConfig().toJSON());
        return redisson;
    }


}
