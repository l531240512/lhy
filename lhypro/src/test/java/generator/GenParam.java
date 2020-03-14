
package generator;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ily
 */
@Data
@AllArgsConstructor
public class GenParam {
	private String module;
	private String[] tables;
}
