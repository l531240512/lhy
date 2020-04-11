package com.lhy.lhypro.service.imp;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.lhypro.dao.TbEmpDao;
import com.lhy.lhypro.po.TbEmp;
import com.lhy.lhypro.service.RedisCacheService;
import com.lhy.lhypro.service.TbEmpService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service("TbEmpServiceImp")
public class TbEmpServiceImp implements TbEmpService {

    @Autowired
    private TbEmpDao dao;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseEntity<TbEmp> findPage(PageBounds pageBounds) {
        return new ResponseEntity<>(dao.selectByPrimaryKey(1), HttpStatus.OK);
    }

    public TbEmp findEntityByName(String userName) {
        log.info("TbEmpServiceImp");
        TbEmp result = null;
        try {
            result = redisCacheService.getTbEmpByName(userName);
        }catch (Exception e){
            log.info("获取用户信息失败");
            e.printStackTrace();
        }
        return result;
    }

    public TbEmp findeEntityById(Integer userId) {
        return dao.selectByPrimaryKey(userId);
    }

    /**
     *@Author: lihuiyang
     *@Date: 3:29 下午 2020/4/11
     *@Description: redis锁
    */
    public void redisLock(){
        stringRedisTemplate.opsForValue().set("stock","10");
        String lockKey = "lockKey";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                int overage = stock - 1;
                stringRedisTemplate.opsForValue().set("stock",String.valueOf(overage));
                log.info("扣减成功，库存stock：{}",overage);
            }else {
                log.info("扣减失败，库存不足");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
