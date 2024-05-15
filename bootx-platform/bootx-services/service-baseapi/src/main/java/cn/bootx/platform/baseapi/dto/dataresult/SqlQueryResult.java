package cn.bootx.platform.baseapi.dto.dataresult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQL查询结果
 * @author xxm
 * @date 2023/4/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "SQL查询结果")
public class SqlQueryResult {

	@Schema(description = "字段列表")
	private List<String> fields = new ArrayList<>();

	@Schema(description = "查询内容")
	private List<Map<String,Object>> data = new ArrayList<>();;
}
