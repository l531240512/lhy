package com.lhy.pro.service.imp;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.pro.common.UsualConstants;
import com.lhy.pro.dao.TbEmpDao;
import com.lhy.pro.po.TbEmp;
import com.lhy.pro.service.RedisCacheService;
import com.lhy.pro.service.TbEmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


@Slf4j
@Service("TbEmpServiceImp1")
public class TbEmpServiceImp1 implements TbEmpService {

    @Autowired
    private TbEmpDao dao;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private WebApplicationContext context;
    
    @Override
    public ResponseEntity<TbEmp> findPage(PageBounds pageBounds) {
        return new ResponseEntity<>(dao.selectByPrimaryKey(1), HttpStatus.OK);
    }

    @Override
    public TbEmp findEntityByName(String userName) {
        log.info("TbEmpServiceImp1");
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
        return null;
    }
}
