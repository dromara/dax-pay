package cn.bootx.platform.baseapi.result.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 数据字典项
 *
 * @author xxm
 * @since 2020/4/15 17:55
 */
@Data
@Accessors(chain = true)
@Schema(title = "数据字典项Dto")
public class DictionaryItemResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字典ID")
    private Long dictId;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典项编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "字典项排序")
    private Double sortNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
