package cn.bootx.platform.baseapi.param.dataresult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2023/3/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "通过SQL查询结果字段请求参数")
public class SqlQueryParam {

    @Schema(description = "数据源Key")
    private String databaseKey;

    @Schema(description = "SQL语句")
    private String sql;

    @Schema(description = "是否启用分页")
    private boolean enablePage;

}
