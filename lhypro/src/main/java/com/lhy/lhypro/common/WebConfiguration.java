package com.lhy.lhypro.common;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.Arrays;
import java.util.List;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/3/14 所属公司：lhy
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                // .exposedHeaders("header1", "header2")
                // .allowCredentials(false)
                // .maxAge(3600)
                .allowedMethods("GET", "POST", "OPTIONS");
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize("2MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("10MB");
        //Sets the directory location where files will be stored.
        //factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
//        ParserConfig.getGlobalInstance().setAsmEnable(false);
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        RestTemplate restTemplate = new RestTemplate();
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        MediaType[] types = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                new MediaType("application", "vnd.spring-boot.actuator.v1+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v2+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v3+json")};
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(types));
        restTemplate.getMessageConverters().add(0, fastJsonHttpMessageConverter);
        return restTemplate;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        MediaType[] types = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                new MediaType("application", "vnd.spring-boot.actuator.v1+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v2+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v3+json")};
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(types));
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter);

    }
}
