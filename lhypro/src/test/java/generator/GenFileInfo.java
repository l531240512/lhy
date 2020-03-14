
package generator;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ily
 */
@Data
@AllArgsConstructor
public class GenFileInfo {
	private String name;
	private String packageName;
	private String path;
}
