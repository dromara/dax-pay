package cn.bootx.platform.starter.code.gen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 代码预览
 *
 * @author xxm
 * @since 2022/2/18
 */
@Data
@Accessors(chain = true)
@Schema(title = "代码预览")
public class CodeGenPreview {

    @Schema(description = "代码名称")
    private String name;

    @Schema(description = "代码内容")
    private String content;

}
