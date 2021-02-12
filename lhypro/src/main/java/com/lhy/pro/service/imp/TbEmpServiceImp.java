package com.lhy.pro.service.imp;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.pro.common.UsualConstants;
import com.lhy.pro.dao.TbEmpDao;
import com.lhy.pro.po.TbEmp;
import com.lhy.pro.service.RedisCacheService;
import com.lhy.pro.service.TbEmpService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


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

    @Autowired
    private WebApplicationContext context;

    @Override
    public ResponseEntity<TbEmp> findPage(PageBounds pageBounds) {
        return new ResponseEntity<>(dao.selectByPrimaryKey(1), HttpStatus.OK);
    }

    @Override
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

    @Override
    public TbEmp findEntityById(Integer userId) {
        return dao.selectByPrimaryKey(userId);
    }

    @Override
    public String asyncMethodWithNoReturnType() {
        TbEmpServiceImp bean = context.getBean(TbEmpServiceImp.class);
        return bean.asyncMethodWithNoReturnType1();
    }

    /**
     *异步方法示例，关键点有三步：
     *  1.启动类增加注解 @EnableAsync
     *  2.当前类声明为服务 @Service
     *  3.方法上面添加注解 @Async
     *限制：
     *   默认类内的方法调用不会被aop拦截，也就是说同一个类内的方法调用，@Async不生效
     *解决办法：
     *  如果要使同一个类中的方法之间调用也被拦截，需要使用spring容器中的实例对象，而不是使用默认的this，因为通过bean实例的调用才会被spring的aop拦截
     *  本例使用方法： AsyncMethod asyncMethod = context.getBean(AsyncMethod.class);    然后使用这个引用调用本地的方法即可达到被拦截的目的
     *备注：
     *  这种方法只能拦截protected，default，public方法，private方法无法拦截。这个是spring aop的一个机制。
     *
     * 默认情况下异步方法的调用使用的是SimpleAsyncTaskExecutor来执行异步方法调用，实际是每个方法都会起一个新的线程。
     * 大致运行过程：（以asyncMethod.bar1();为例）
     *  1.调用bar1()方法被aop拦截
     *  2.使用cglib获取要执行的方法和入参、当前实例（后续用于反射调用方法）。这些是运行一个方法的必要条件，可以封装成独立的方法来运行
     *  3.启动新的线程，调用上面封装的实际要调用的方法
     *  4.返回方法调用的结果
     *  前提是启动的时候被spring提前处理，将方法进行封装，加载流程：
     *    AsyncAnnotationBeanPostProcessor ->
     * 如果要修改@Async异步方法底层调用：
     *  可以实现AsyncConfigurer接口，或者提供TaskExecutor实例（然后在@Async中指定这个实例），详见本例代码
     *
     * 异步方法返回类型只能有两种：void和java.util.concurrent.Future
     *  当返回类型为void的时候，方法调用过程产生的异常不会抛到调用者层面，可以通过注册AsyncUncaughtExceptionHandler来捕获此类异常
     *  当返回类型为Future的时候，方法调用过程差生的异常会抛到调用者层面
     *
     * @DESC
     * @author guchuang
     *
     */
    @Async
    public String asyncMethodWithNoReturnType1() {
        try {
            Thread.sleep(3000);
            return UsualConstants.SUCCESS;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
