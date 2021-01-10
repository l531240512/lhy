package com.lhy.pro.dao;


import com.lhy.pro.po.TbEmp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * 表tb_emp对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 */
@Component
@Mapper
public interface TbEmpDao extends MyBatisBaseDao<TbEmp,Integer> {
    TbEmp selectByName(@Param("name") String name);
}
