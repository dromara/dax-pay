package cn.bootx.platform.baseapi.param.chinaword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 敏感词验证参数类
 * @author xxm
 * @since 2023/8/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "敏感词验证参数类")
public class ChinaWordVerifyParam {
    @Schema(description = "文本")
    private String text;

    @Schema(description = "间隔距离")
    private int skip = 0;

    @Schema(description = "替换字符")
    private char symbol = '*';
}
