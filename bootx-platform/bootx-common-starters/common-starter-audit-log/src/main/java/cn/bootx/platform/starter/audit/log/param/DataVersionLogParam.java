package cn.bootx.platform.starter.audit.log.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/1/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "数据版本日志")
public class DataVersionLogParam {

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "数据名称")
    private String dataName;

    @Schema(description = "数据主键")
    private String dataId;

    @Schema(description = "数据内容对象")
    private Object dataContent;

    @Schema(description = "本次变动的数据内容")
    private Object changeContent;

    @Schema(description = "版本")
    private Integer version;

}
