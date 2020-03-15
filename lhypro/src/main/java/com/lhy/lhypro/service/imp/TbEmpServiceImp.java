package com.lhy.lhypro.service.imp;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.lhypro.dao.TbEmpDao;
import com.lhy.lhypro.po.TbEmp;
import com.lhy.lhypro.service.RedisCacheService;
import com.lhy.lhypro.service.TbEmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
