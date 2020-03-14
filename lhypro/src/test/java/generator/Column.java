
package generator;

import lombok.Data;

/**
 * @author ily
 */
@Data
public class Column {
	private String name;
	private String type;
	private int size;
	private String defaultValue;
	private String comment;
	private boolean nullable;
	private int decimalDigits;
}
