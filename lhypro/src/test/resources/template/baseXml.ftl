<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- ============================================================== -->
<!-- ============================================================== -->
<!-- =======通过工具自动生成，请勿手工修改！======= -->
<!-- =======本配置文件中定义的节点可在自定义配置文件中直接使用！       ======= -->
<!-- ============================================================== -->
<!-- ============================================================== -->
<mapper namespace="${daoInfoPackage}">
	<!-- 默认开启二级缓存,使用Least Recently Used（LRU，最近最少使用的）算法来收回 -->
	<!-- <cache/> -->
	<!-- 通用查询结果对象-->
	<resultMap id="BaseResultMap" type="${poInfoPackage}">
		<#if resultMaps??>
        <#list resultMaps as resultMap>
            ${resultMap}
        </#list>
        </#if>
	</resultMap>

	<!-- 通用查询结果列-->
	<sql id="Base_Column_List">
			 <#list columnLists as column>${column}<#if column_has_next>,</#if></#list>
	</sql>
	<!-- 按对象查询记录的WHERE部分-->
	<sql id="Base_Select_By_Entity_Where">
		<#list entityWheres?keys as key>
			<if test="${entityWheres["${key}"]} != null and ${entityWheres["${key}"]} != ''">
				and ${key} = ${r'#{'+entityWheres["${key}"]+'}'}
			</if>
        </#list>
	</sql>
	<!-- 按对象查询记录的SQL部分 -->
	<sql id="Base_Select_By_Entity">
		select
		<include refid="Base_Column_List"/>
		from ${tableName}
		<where>
			<include refid="Base_Select_By_Entity_Where"/>
		</where>
	</sql>

	<!-- 按主键查询一条记录 -->
	<select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="map">
		select
		<include refid="Base_Column_List"/>
		from ${tableName}
		where id = ${r'#{param1}'}
	</select>
	<!-- 按主键List查询多条记录 -->
	<select id="selectBatchByPrimaryKeys" resultMap="BaseResultMap" parameterType="map">
		select
		<include refid="Base_Column_List"/>
		from  ${tableName}
		where id in
		<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
        ${r'#{item}'}
		</foreach>
	</select>
	<!-- 按对象查询一页记录（多条记录） -->
	<select id="selectPage" resultMap="BaseResultMap" parameterType="${poInfoPackage}">
		<include refid="Base_Select_By_Entity"/>
	</select>

	<!-- 按对象查询一页记录（多条记录） -->
	<select id="selectAll" resultMap="BaseResultMap" parameterType="${poInfoPackage}">
		<include refid="Base_Select_By_Entity"/>
	</select>
	<!-- 按主键删除一条记录 -->
	<delete id="deleteByPrimaryKey" parameterType="map">
		delete from ${tableName} where id = ${r'#{param1}'}
	</delete>
	<!-- 按主键List删除多条记录 -->
	<delete id="deleteBatchByPrimaryKeys" parameterType="map">
		delete from ${tableName}
		where id in
		<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
        ${r'#{item}'}
		</foreach>
	</delete>
	<!-- 完整插入一条记录-->
	<insert id="insert" parameterType="${poInfoPackage}" useGeneratedKeys="true" keyProperty="id">
		insert into ${tableName}
		(<#list entitySet?keys as key>${key}<#if key_has_next>,</#if></#list>)
		values(<#list entitySet?values as value>${value}<#if value_has_next>,</#if></#list>)
	</insert>
	<!-- 插入一条记录(为空的字段不操作) -->
	<insert id="insertSelective" parameterType="${poInfoPackage}" useGeneratedKeys="true" keyProperty="id">
		insert into ${tableName}
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<#list entitySet?keys as key>
				<if test="${entitySet["${key}"]} != null">
                    ${key},
				</if>
            </#list>
		</trim>
		values
		<trim prefix="(" suffix=")" suffixOverrides=",">
		<#list entitySet?values as value>
			<if test="${value} != null">
                ${r'#{'}${value}},
			</if>
        </#list>
		</trim>
	</insert>
	<!-- 更新一条记录(为空的字段不操作) -->
	<update id="updateSelectiveByPrimaryKey" parameterType="${poInfoPackage}">
		update ${tableName}
		<set>
			<#list entitySet?keys as key>
				<if test="${entitySet["${key}"]} != null">
                    ${key}=${entitySet["${key}"]},
				</if>
            </#list>
		</set>
		where id = ${r'#{id}'}
	</update>
	<!-- 完整更新一条记录 -->
	<update id="updateByPrimaryKey" parameterType="${poInfoPackage}">
		update ${tableName}
		set <#list entitySet?keys as key>${key}= ${r'#{'}${entitySet["${key}"]}}<#if key_has_next>,</#if></#list>
		where id = ${r'#{id}'}
	</update>

	<select id="count" resultType="java.lang.Integer" parameterType="map">
		SELECT count(1) FROM ${tableName}
		<where>
			<include refid="Base_Select_By_Entity_Where"/>
		</where>
	</select>
</mapper>