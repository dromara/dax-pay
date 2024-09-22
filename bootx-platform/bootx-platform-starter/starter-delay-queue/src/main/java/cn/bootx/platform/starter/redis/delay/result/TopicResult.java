package cn.bootx.platform.starter.redis.delay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 主题信息
 * @author xxm
 * @since 2024/9/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "主题信息")
public class TopicResult {

    @Schema(description = "主题名称")
    private String name;

    /** 数量 */
    @Schema(description = "数量")
    private Integer count;
}
