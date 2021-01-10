package com.lhy.pro.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MyBatisBaseDao<T, I> {
    int insert(T var1);

    int insertSelective(T var1);

    int deleteByPrimaryKey(I var1);

    int deleteBatchByPrimaryKeys(List<I> var1);

    int updateByPrimaryKey(T var1);

    int updateSelectiveByPrimaryKey(T var1);

    T selectByPrimaryKey(I var1);

    List<T> selectBatchByPrimaryKeys(List<I> var1);

    List<T> selectPage(PageBounds var1, T var2);
}
