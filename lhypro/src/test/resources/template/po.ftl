package ${packageName};


<#list imports as import>
${import}
</#list>

/**
 *
 * 通过工具自动生成，请勿手工修改。表${tableName}的PO对象<br/>
 * ${classComment}
 *
 */
@Data
public class ${beanName} implements Serializable {
	private static final long serialVersionUID = 1L;

<#list fields as field>
	${field.attribute}
</#list>

}
