package com.lhy.lhypro.service;

import com.lhy.lhypro.dao.TbEmpDao;
import com.lhy.lhypro.po.TbEmp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/3/14 所属公司：lhy
 */
@Slf4j
@Component
public class RedisCacheService {
    @Autowired
    private TbEmpDao dao;

    @Cacheable(cacheNames = "tbEmp", key = "'tbEmp'.concat(#name)")
    public TbEmp getTbEmpByName(String name) throws Exception {
        return dao.selectByName(name);
    }
    @CacheEvict(value="tbEmp" ,key = "'tbEmp'.concat(#name)")
    public void deleteTbEmp(String name) {
        log.info("清除缓存数据！");
        return;
    }
}
