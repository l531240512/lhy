
package generator;

import lombok.Data;

import java.util.List;

/**
 * @author ily
 */
@Data
public class Table {
	private String name;
	private String comment;
	private List<Column> columns;
	private List<PrimaryKey> primaryKeys;
}
