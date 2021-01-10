package com.lhy.pro.service.imp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2020/4/11 所属公司：lhy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TbEmpServiceImpTest {
    @Autowired
    private TbEmpServiceImp tbEmpServiceImp;

    @Test
    public void redisLock() {
        tbEmpServiceImp.redisLock();
    }
}
