package cn.bootx.platform.starter.redis.delay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 延时桶信息
 * @author xxm
 * @since 2024/9/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "延时桶信息")
public class BucketResult {

    @Schema(description = "桶名称")
    private String name;

    /** 数量 */
    @Schema(description = "数量")
    private Integer count;
}
