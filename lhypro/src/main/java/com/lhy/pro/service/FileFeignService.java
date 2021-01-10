package com.lhy.pro.service;

import io.swagger.annotations.ApiImplicitParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 项目名字：lhy
 * 调用其他服务的上传附件接口
 * @author lhy
 */
@FeignClient(name = "FileFeignService",url="${fileFeignServiceUrl}")
@Mapper
public interface FileFeignService {
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"}, consumes = {"multipart/form-data"})
    @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String")
    @ResponseBody
    String upload(@RequestParam(value = "fileName") String fileName, @RequestPart("file") MultipartFile file);
}
