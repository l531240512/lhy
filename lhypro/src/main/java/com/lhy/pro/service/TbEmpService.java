package com.lhy.pro.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.pro.po.TbEmp;
import org.springframework.http.ResponseEntity;



public interface TbEmpService {

    public ResponseEntity<TbEmp> findPage(PageBounds pageBounds);

    public TbEmp findEntityByName(String userName);

    public TbEmp findeEntityById(Integer userId);
}
