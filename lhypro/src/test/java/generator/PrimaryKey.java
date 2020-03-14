
package generator;

import lombok.Data;

/**
 * @author ily
 */
@Data
public class PrimaryKey {
	private String pkName;
	private int keySeq;
	private String columnName;
}
