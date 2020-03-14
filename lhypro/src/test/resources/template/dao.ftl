package ${packageName};


import org.apache.ibatis.annotations.Mapper;

import ${poInfoPackage};



/**
 * 表${tableName}对应的基于MyBatis实现的Dao接口<br/>
 * 在其中添加自定义方法
 */
@Mapper
public interface ${daoInfoName} extends MyBatisBaseDao<${poInfoName}, ${idColumnType}> {
}