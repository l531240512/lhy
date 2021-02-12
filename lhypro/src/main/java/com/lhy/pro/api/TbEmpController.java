package com.lhy.pro.api;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.pro.po.TbEmp;
import com.lhy.pro.service.TbEmpService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lhy
 */
@RestController()
@Api(tags = "接口", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/tb_emp")
public class TbEmpController {

    @Autowired
    @Qualifier("TbEmpServiceImp1")
    private TbEmpService service1;

    @Autowired
    @Qualifier("TbEmpServiceImp")
    private TbEmpService service;

    @GetMapping("/pager")
    public ResponseEntity<TbEmp> findPager(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                                           @RequestParam(required = false) String sort) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        return service.findPage(pageBounds);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<TbEmp> findEntityByUserName(@PathVariable("userName") String userName) {
        return new ResponseEntity(service.findEntityByName(userName), HttpStatus.OK);
    }

    @GetMapping("/s1/{userName}")
    public ResponseEntity<TbEmp> findEntityByUserName1(@PathVariable("userName") String userName) {
        return new ResponseEntity(service1.findEntityByName(userName), HttpStatus.OK);
    }

    @GetMapping("/callWithNoReturnType")
    public ResponseEntity<String> callWithNoReturnType() {
        service.asyncMethodWithNoReturnType();
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
