package com.lhy.pro.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author lhy
 *
 */
@Slf4j
public class FileUtil {
    /**
     * 获取jar包项目ClassPath下的文件
     * @param filePath
     * @return
     * @throws Exception
     */
    public File getClassPathFile(String filePath) throws Exception {
        File file = null;
        log.info("开始获取classPath下的文件");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 获取远程服务器IP和端口
        //获取所有匹配的文件
        Resource[] resources = resolver.getResources(File.separator + filePath);
        for(Resource resource : resources) {
            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
            InputStream stream = resource.getInputStream();
            if (log.isInfoEnabled()) {
                log.info("读取的文件流  [{}]",stream);
            }
            String targetFilePath = resource.getFilename();
            if (log.isInfoEnabled()) {
                log.info("放置位置  [{}]",targetFilePath);
            }
            file = new File(targetFilePath);
            FileUtils.copyInputStreamToFile(stream, file);
            if(file != null) {
                break;
            }
        }
        log.info("获取classPath下的文件结束！");
        return file;
    }
}
