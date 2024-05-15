package cn.bootx.platform.baseapi.dto.dataresult;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xxm
 * @since 2023/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "SQL查询语句")
public class QuerySqlDto extends BaseDto {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "数据源ID")
    private Long databaseId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "sql语句")
    private String sql;

    @Schema(description = "是否集合")
    private Boolean isList;

    @Schema(description = "SQL查询参数")
    private List<SqlParam> params;

    @Schema(description = "SQL查询结果字段")
    private List<SqlField> fields;

}
