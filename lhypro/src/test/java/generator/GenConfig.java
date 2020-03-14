
package generator;

import lombok.Data;

/**
 * @author ily
 */
@Data
public class GenConfig {
	private String basePackage;
	protected String saveDir;
	protected String saveDirForVo;
	protected String saveDirForXml;
	protected String saveDirForHtml;
	private GenType[] genTypes;
	protected String[] tableNames;
	protected boolean fileOverride;
	protected boolean dbPrefix;
	protected boolean dbColumnUnderline;
	protected String dbDriverName;
	protected String dbUser;
	protected String dbPassword;
	protected String dbUrl;
	protected String dbSchema;
	
	public GenConfig() {
		this.tableNames = null;
		
		this.fileOverride = true;
		
		this.dbPrefix = false;
		
		this.dbColumnUnderline = false;
	}
}
