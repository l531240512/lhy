
package generator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ily
 */
@Data
public class Field {
	private String comment;
	private String attribute;
	
	public Field(Column column, Generator generator) {
		this.setComment(column, generator);
		this.setAttribute(column, generator);
	}
	
	public void setComment(Column column, Generator generator) {
		String comment = column.getComment();
		if (StringUtils.isNotBlank(comment)) {
			String[] comments = comment.split("@");
			this.comment = String.format("/**对应字段：%s,备注：%s*/", column.getName(), comments[0].trim());
		}
	}
	
	public void setAttribute(Column column, Generator generator) {
		String field = generator.processField(column.getName());
		this.attribute = String.format("private %s %s;", generator.processType(column.getType()), field);
	}
}
