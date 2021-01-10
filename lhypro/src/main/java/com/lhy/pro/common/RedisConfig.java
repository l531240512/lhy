package com.lhy.pro.common;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2019/12/10 所属公司：lhy
 */
@Configuration
public class RedisConfig {
    @Bean
    @Primary
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        //使用fastjson序列化
//        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
//        // value值的序列化采用fastJsonRedisSerializer
//        template.setValueSerializer(fastJsonRedisSerializer);
//        template.setHashValueSerializer(fastJsonRedisSerializer);
//        // key的序列化采用StringRedisSerializer
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setConnectionFactory(redisConnectionFactory);
        // 使用fastjson序列化替代jdk序列化
        RedisSerializer<Object> redisSerializer = getRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    private RedisSerializer<Object> getRedisSerializer(){
        return new GenericFastJsonRedisSerializer();
    }

    @Bean
    @Primary//当有多个管理器的时候，必须使用该注解在一个管理器上注释：表示该管理器为默认的管理器
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //序列化方式1
        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下(4行)注释代码为默认实现
//        ClassLoader loader = this.getClass().getClassLoader();
//        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
//        RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        //序列化方式1---另一种实现方式
        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();//该语句相当于序列化方式1

        //序列化方式2
//        Jackson2JsonRedisSerializer serializer=new Jackson2JsonRedisSerializer(Object.class);
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
//        RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        //序列化方式3  JSONObject
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//        //设置过期时间 30天
        /**
         *  以下两部分设置为了兼容springboot1.*整合redis
         *
         *  @Cacheable(cacheNames = "tbEmp", key = "'tbEmp'.concat(#name)")向缓存中存数据
         * disableKeyPrefix() : 设置向缓存写数据时，去掉一层文件加
         * computePrefixWith((name) -> { return name + ":"; }) 设置key 和 cacheNames 之间以 ：分割
         * */
        defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofDays(30)).disableKeyPrefix().computePrefixWith((name) -> {
            return name + ":";
        });
//        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
//        //设置白名单---非常重要********
//        /*
//        使用fastjson的时候：序列化时将class信息写入，反解析的时候，
//        fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
//        如果序列化信息中的类路径不在autoType中，
//        反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
//        可参考 https://blog.csdn.net/u012240455/article/details/80538540
//        */
//        ParserConfig.getGlobalInstance().addAccept("ins.ht.");
        return cacheManager;
    }
}
