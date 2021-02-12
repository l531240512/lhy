package com.lhy.pro.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.pro.po.TbEmp;
import org.springframework.http.ResponseEntity;



public interface TbEmpService {

     ResponseEntity<TbEmp> findPage(PageBounds pageBounds);

     TbEmp findEntityByName(String userName);

     TbEmp findEntityById(Integer userId);

     String asyncMethodWithNoReturnType();
}
