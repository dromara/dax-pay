package cn.bootx.platform.baseapi.dto.chinaword;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 敏感词
 * @author xxm
 * @since 2023-08-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "敏感词")
@Accessors(chain = true)
public class ChinaWordDto extends BaseDto {

    @Schema(description = "敏感词")
    private String word;
    @Schema(description = "分类")
    private String type;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "是否启用")
    private Boolean enable;
    @Schema(description = "是否是白名单名词")
    private Boolean white;
}
