
package generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ily
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class Generator extends BaseGenerator {
	private GenFileInfo voInfo;
	private GenFileInfo poInfo;
	private GenFileInfo daoInfo;
	private GenFileInfo serviceInfo;
	private GenFileInfo controllerInfo;
	private GenFileInfo baseMapperXmlInfo;
	private GenFileInfo mapperXmlInfo;
	private GenFileInfo htmlInfo;

	private String assemblePackage(String module, String catalog) {
		String result = this.genConfig.getBasePackage() + "." + module;
		if (StringUtils.isNotBlank(catalog)) {
			result = result + "." + catalog;
		}
		return result;
	}

	private String assembleXmlPackage(String module) {
		String result = "";
		if (StringUtils.isNotBlank(module)) {
			result = module;
		} else {
			result = "misc";
		}
		return result;
	}

	private void resetFileInfo(String beanName, String module) {
		String saveDir = this.genConfig.getSaveDir();
		String name = beanName + "VO";
		String packageName = assemblePackage(module, "vo");
		String path = getFilePath(this.genConfig.getSaveDirForVo(), getPathFromPackageName(packageName));
		this.voInfo = new GenFileInfo(name, packageName, path);
		name = beanName;
		packageName = assemblePackage(module, "po");
		path = getFilePath(saveDir, getPathFromPackageName(packageName));
		this.poInfo = new GenFileInfo(name, packageName, path);
		name = beanName + "Dao";
		packageName = assemblePackage(module, "dao");
		path = getFilePath(saveDir, getPathFromPackageName(packageName));
		this.daoInfo = new GenFileInfo(name, packageName, path);
		name = beanName + "Service";
		packageName = assemblePackage(module, "service");
		path = getFilePath(saveDir, getPathFromPackageName(packageName));
		this.serviceInfo = new GenFileInfo(name, packageName, path);
		name = beanName + "Controller";
		packageName = assemblePackage(module, "api");
		path = getFilePath(saveDir, getPathFromPackageName(packageName));
		this.controllerInfo = new GenFileInfo(name, packageName, path);
		name = beanName + "BaseDao";
		packageName = assembleXmlPackage(module);
		String xmlPath = getFilePath(this.genConfig.getSaveDirForXml(), "base");
		path = getFilePath(xmlPath, getPathFromPackageName(packageName));
		this.baseMapperXmlInfo = new GenFileInfo(name, packageName, path);
		name = beanName + "Dao";
		packageName = assembleXmlPackage(module);
		xmlPath = getFilePath(this.genConfig.getSaveDirForXml(), "custom");
		path = getFilePath(xmlPath, getPathFromPackageName(packageName));
		this.mapperXmlInfo = new GenFileInfo(name, packageName, path);
		if (StringUtils.isNotBlank(this.genConfig.getSaveDirForHtml())) {
			name = beanName;
			path = getFilePath(this.genConfig.getSaveDirForHtml(), null);
			this.voInfo = new GenFileInfo(name, packageName, path);
		}
	}

	@Override
	protected void run(Table table, String module) throws Exception {
		System.out.println("============处理表" + table.getName() + "==================");
		if (table.getPrimaryKeys().size() == 0) {
			System.out.println("表" + table.getName() + "没有主键字段，忽略生成，请手工编写.");
			return;
		}
		if (table.getPrimaryKeys().size() > 1) {
			System.out.println("表" + table.getName() + "为联合主键，忽略生成,请手工编写.");
			return;
		}
		String beanName = getBeanName(table.getName(), false);
		resetFileInfo(beanName, module);
		if (containsGenType(GenType.VO) && (validFile(this.voInfo.getPath(), this.voInfo.getName(), ".java"))) {
			buildVo(table);
		}
		this.fileOvervide = true;
		if (containsGenType(GenType.PO)
				&& (validFile(this.poInfo.getPath(), this.poInfo.getName(), ".java"))) {
			buildPo(table);
		}
		this.fileOvervide = false;
		if (containsGenType(GenType.DAO)
				&& (validFile(this.daoInfo.getPath(), this.daoInfo.getName(), ".java"))) {
			buildDao(table);
		}
		this.fileOvervide = false;
		if (containsGenType(GenType.SERVICE) && (validFile(this.serviceInfo.getPath(), this.serviceInfo.getName(), ".java"))) {
			buildService(table);
		}
		this.fileOvervide = false;
		if (containsGenType(GenType.CONTROLLER) && (validFile(this.controllerInfo.getPath(), this.controllerInfo.getName(), ".java"))) {
			buildController(table);
		}
		this.fileOvervide = true;
		if (containsGenType(GenType.BASE_MAPPER_XML)
				&& (validFile(this.baseMapperXmlInfo.getPath(), this.baseMapperXmlInfo.getName(),
				".xml"))) {
			buildBaseXml(table);
		}
		this.fileOvervide = false;
		if (containsGenType(GenType.MAPPER_XML)
				&& (validFile(this.mapperXmlInfo.getPath(), this.mapperXmlInfo.getName(), ".xml"))) {
			buildXml(table);
		}
		this.fileOvervide = false;
		if (containsGenType(GenType.HTML)) {
			buildHtml(table);
		}

	}

	private List<String> getTableColumnTypes(Table table) {
		List types = new ArrayList();
		List columns = table.getColumns();
		int size = columns.size();
		for (int i = 0; i < size; ++i) {
			Column column = (Column) columns.get(i);
			types.add(column.getType());
		}
		return types;
	}

	private void buildHtml(Table table) {


	}

	private void buildXml(Table table) {
		File mapperXmlFile = new File(this.mapperXmlInfo.getPath(),
				this.mapperXmlInfo.getName() + ".xml");
		Map<String, Object> root = new HashMap<>();
		root.put("daoInfoPackage", this.daoInfo.getPackageName() + "." + this.daoInfo.getName());
		try {
			createFile(root, "customXml.ftl", mapperXmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildBaseXml(Table table) {
		File mapperBaswXmlFile = new File(this.baseMapperXmlInfo.getPath(),
				this.baseMapperXmlInfo.getName() + ".xml");
		List primaryKeys = table.getPrimaryKeys();
		PrimaryKey primaryKey = (PrimaryKey) primaryKeys.get(0);
		Map<String, Object> root = new HashMap<>();
		root.put("poInfoPackage", this.poInfo.getPackageName() + "." + this.poInfo.getName());
		root.put("daoInfoPackage", this.daoInfo.getPackageName() + "." + this.daoInfo.getName());
		root.put("tableName", table.getName());
		buildBaseDaoSQLBaseResultMap(root, primaryKey, table.getColumns());
		buildBaseDaoSQLBaseColumnList(root, table.getColumns());
		buildBaseDaoSQLBaseSelectByEntityWhere(root, table.getColumns());
		buildBaseDaoSQLBaseSelectByEntityWhere(root, table.getColumns(),primaryKey);
		try {
			createFile(root, "baseXml.ftl", mapperBaswXmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildController(Table table) {
		Column idColumn = checkPrimaryKeys(table);
		File controllerFile = new File(this.controllerInfo.getPath(), this.controllerInfo.getName() + ".java");
		Map<String, Object> root = new HashMap<>();
		root.put("poInfoPackage", this.poInfo.getPackageName() + "." + this.poInfo.getName());
		root.put("serverInfoPackage", this.serviceInfo.getPackageName() + "." + this.serviceInfo.getName());
		root.put("packageName", this.getControllerInfo().getPackageName());
		root.put("poInfoName", this.poInfo.getName());
		root.put("listUrl", this.poInfo.getName().toLowerCase());
		root.put("idColumnType", processType(idColumn.getType()));
		root.put("controllerInfoName", this.controllerInfo.getName());
		root.put("serverInfoName", this.serviceInfo.getName());
		try {
			createFile(root, "api.ftl", controllerFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildService(Table table) {
		Column idColumn = checkPrimaryKeys(table);
		File serviceFile = new File(this.serviceInfo.getPath(), this.serviceInfo.getName() + ".java");
		Map<String, Object> root = new HashMap<>();
		root.put("poInfoPackage", this.poInfo.getPackageName() + "." + this.poInfo.getName());
		root.put("daoInfoPackage", this.daoInfo.getPackageName() + "." + this.daoInfo.getName());
		root.put("packageName", this.serviceInfo.getPackageName());
		root.put("poInfoName", this.poInfo.getName());
		root.put("serviceInfoName", this.serviceInfo.getName());
		root.put("daoInfoName", this.daoInfo.getName());
		root.put("idColumnType", processType(idColumn.getType()));
		try {
			createFile(root, "server.ftl", serviceFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void buildDao(Table table) {
		Column idColumn = checkPrimaryKeys(table);
		File mapperFile = new File(this.daoInfo.getPath(), this.daoInfo.getName() + ".java");
		Map<String, Object> root = new HashMap<>();
		root.put("packageName", this.daoInfo.getPackageName());
		root.put("poInfoPackage", this.poInfo.getPackageName() + "." + this.poInfo.getName());
		root.put("tableName", table.getName());
		root.put("poInfoName", this.poInfo.getName());
		root.put("daoInfoName", this.daoInfo.getName());
		root.put("idColumnType", processType(idColumn.getType()));
		try {
			createFile(root, "dao.ftl", mapperFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildPo(Table table) {
		List types = getTableColumnTypes(table);
		File beanFile = new File(this.poInfo.getPath(), this.poInfo.getName() + ".java");
		Map<String, Object> root = new HashMap<>();
		root.put("packageName", this.poInfo.getPackageName());
		root.put("beanName", this.poInfo.getName());
		getImports(root, types);
		getClassComment(root, table);
		getColumn(root, table);
		try {
			createFile(root, "po.ftl", beanFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildVo(Table table) {
		List types = getTableColumnTypes(table);
		File beanFile = new File(this.voInfo.getPath(), this.voInfo.getName() + ".java");
		Map<String, Object> root = new HashMap<>();
		root.put("packageName", this.voInfo.getPackageName());
		root.put("beanName", this.voInfo.getName());
		getImports(root, types);
		getClassComment(root, table);
		getColumn(root, table);
		try {
			createFile(root, "po.ftl", beanFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
		}
	}


	private void getClassComment(Map<String, Object> root, Table table) {
		String classComment = "对应表名：" + table.getName();
		if (StringUtils.isNotBlank(table.getComment())) {
			classComment += ",备注：" + table.getComment().trim();
		}
		root.put("tableName", table.getName());
		root.put("classComment", classComment);
	}


	public void getColumn(Map<String, Object> root, Table table) {
		List<Column> columns = table.getColumns();
		List<Field> fields = new ArrayList<>();
		columns.forEach(column -> {
			fields.add(new Field(column, this));
		});
		root.put("fields", fields);
	}

	private void getImports(Map<String, Object> root, List<String> types) {
		List<String> imports = new ArrayList<>();
		imports.addAll(Arrays.asList("import java.io.Serializable;", "import lombok.Data;"));
		types.forEach(type -> {
			if (isDate(types) && !imports.contains("import java.util.Date;")) {
				imports.add("import java.util.Date;");
			}
			if (isDecimal(types) && !imports.contains("import java.math.BigDecimal;")) {
				imports.add("import java.math.BigDecimal;");
			}
		});
		root.put("imports", imports);
	}

	private void createFile(Map<String, Object> root, String templateName, File file) throws Exception {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setDirectoryForTemplateLoading(new File(
				Thread.currentThread().getContextClassLoader().getResource("").getPath()
						+ "template"));
		cfg.setDefaultEncoding("UTF-8");
		Template template = cfg.getTemplate(templateName);
		// 获取io流 输出 关流
		@Cleanup Writer out = new FileWriter(file);
		template.process(root, out);
	}

	private Column checkPrimaryKeys(Table table) {
		List primaryKeys = table.getPrimaryKeys();
		if (primaryKeys.size() != 1) {
			throw new IllegalArgumentException("目前只支持单一主键的表");
		}
		PrimaryKey primaryKey = (PrimaryKey) primaryKeys.get(0);
		Column idColumn = null;
		List columns = table.getColumns();
		int size = columns.size();
		for (int i = 0; i < size; ++i) {
			Column column = (Column) columns.get(i);
			if (column.getName().equalsIgnoreCase(primaryKey.getColumnName())) {
				idColumn = column;
				break;
			}
		}
		if (idColumn == null) {
			throw new IllegalArgumentException("找不到主键名对应的字段");
		}
		return idColumn;
	}

	protected void buildBaseDaoSQLBaseResultMap(Map<String, Object> root, PrimaryKey primaryKey, List<Column> columns) {
		// Postgre, Oracle由于sql查询中使用as不区分大小写，不能写resultMap与属性的对应名
		if (this.genConfig.getDbDriverName().contains("mysql")) {
			List<String> resultMaps = columns.stream().map(column -> {
				String prefix = column.getName().equalsIgnoreCase(primaryKey.getColumnName()) ? "id" : "result";
				return String.format("<%s column=\"%s\" property=\"%s\"/>", prefix, column.getName(), processField(column.getName()));
			}).collect(Collectors.toList());
			root.put("resultMaps", resultMaps);
		}
	}

	protected void buildBaseDaoSQLBaseColumnList(Map<String, Object> root, List<Column> columns) {
		List<String> columnLists = columns.stream().map(column -> column.getName().contains("_") ? String.format("%s AS %s", column.getName(), processField(column.getName())) : column.getName()).collect(Collectors.toList());
		root.put("columnLists", columnLists);
	}


	protected void buildBaseDaoSQLBaseSelectByEntityWhere(Map<String, Object> root, List<Column> columns) {
		Map<String, String> entityWheres = columns.stream().collect(Collectors.toMap(Column::getName, column -> processField(column.getName())));
		root.put("entityWheres", entityWheres);
	}

	protected void buildBaseDaoSQLBaseSelectByEntityWhere(Map<String, Object> root, List<Column> columns, PrimaryKey primaryKey) {
		Map<String, String> entitySet = columns.stream().filter(column -> !column.getName().equalsIgnoreCase(primaryKey.getColumnName())).collect(Collectors.toMap(Column::getName, column -> processField(column.getName())));
		root.put("entitySet", entitySet);
	}
}
