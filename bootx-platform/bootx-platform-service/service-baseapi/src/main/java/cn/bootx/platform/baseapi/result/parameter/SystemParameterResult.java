package cn.bootx.platform.baseapi.result.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统参数
 *
 * @author xxm
 * @since 2021/10/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统参数")
public class SystemParameterResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "参数名称")
    private String name;

    @Schema(description = "参数键名")
    private String key;

    @Schema(description = "参数值")
    private String value;

    @Schema(description = "参数类型")
    private Integer type;

    @Schema(description = "启用状态")
    private boolean enable;

    @Schema(description = "是否系统参数")
    private boolean internal;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
