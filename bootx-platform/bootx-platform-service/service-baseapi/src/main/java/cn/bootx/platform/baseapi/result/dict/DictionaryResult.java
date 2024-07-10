package cn.bootx.platform.baseapi.result.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2020/4/10 14:46
 */
@Data
@Accessors(chain = true)
@Schema(title = "数据字典目录")
public class DictionaryResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "分类标签")
    private String groupTag;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
