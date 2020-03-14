package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名字：lhy
 *
 * @author 作者: lihuiyang 功能描述：1. 创建时间: 2019-08-30 所属公司：lhy
 */
@RestController
public class IndexController {
    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String index() throws Exception {
        String appflag = env.getProperty("spring.profiles.active");
        String application_name = env.getProperty("spring.application.name");
        String info = "profiles-active: ["+appflag+"]; application-name: ["+application_name+"]; RUNNING...";
        return info;
    }
}
