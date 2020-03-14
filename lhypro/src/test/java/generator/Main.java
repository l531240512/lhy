/**
 * Created by Enzo Cotter on 2019/1/3.
 */
package generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li Ying, 2019骞� 01鏈�03鏃�
 * @version GD v1.0
 */
public class Main {
	public static void main(String[] args) {
		List<GenParam> paramList = new ArrayList<>();
		paramList.add(new GenParam("lhypro", new String[]{"tb_emp"}));
		GenConfig gc = new GenConfig();
		gc.setBasePackage("com.lhy");
		gc.setSaveDir("/Users/lhy/IdeaProjects/lhypro/src/main/java");
		gc.setDbDriverName("com.mysql.cj.jdbc.Driver");
		gc.setDbUser("root");
		gc.setDbSchema("demo");
		gc.setDbPassword("12345678");
		gc.setDbUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
//		gc.setGenTypes(new GenType[] { GenType.VO, GenType.PO, GenType.SERVICE, GenType.DAO, GenType.BASE_MAPPER_XML, GenType.MAPPER_XML ,GenType.CONTROLLER});
		gc.setGenTypes(new GenType[] { GenType.PO, GenType.MAPPER_XML, GenType.BASE_MAPPER_XML});
		// gc.setGenTypes(new GenType[] { GenType.VUE});
		Generator generator = new Generator();
		generator.setGenConfig(gc);
		generator.setParamList(paramList);
		generator.generate();
	}
}
