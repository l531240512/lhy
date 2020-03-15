package com.lhy.lhypro.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.lhypro.po.TbEmp;
import org.springframework.http.ResponseEntity;



public interface TbEmpService {

    public ResponseEntity<TbEmp> findPage(PageBounds pageBounds);

    public TbEmp findEntityByName(String userName);

    public TbEmp findeEntityById(Integer userId);
}
