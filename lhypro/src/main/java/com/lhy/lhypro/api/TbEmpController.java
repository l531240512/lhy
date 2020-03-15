package com.lhy.lhypro.api;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.lhy.lhypro.po.TbEmp;
import com.lhy.lhypro.service.TbEmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TbEmpController {

    @Autowired
    @Qualifier("TbEmpServiceImp1")
    private TbEmpService service1;

    @Autowired
    @Qualifier("TbEmpServiceImp")
    private TbEmpService service;

    @GetMapping("/tbemp/pager")
    public ResponseEntity<TbEmp> findPager(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                                           @RequestParam(required = false) String sort) {
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sort));
        return service.findPage(pageBounds);
    }

    @GetMapping("/tbemp/{userName}")
    public ResponseEntity<TbEmp> findPEntityByUserName(@PathVariable("userName") String userName) {
        return new ResponseEntity(service.findEntityByName(userName), HttpStatus.OK);
    }

    @GetMapping("/tbemp/s1/{userName}")
    public ResponseEntity<TbEmp> findPEntityByUserName1(@PathVariable("userName") String userName) {
        return new ResponseEntity(service1.findEntityByName(userName), HttpStatus.OK);
    }
}
