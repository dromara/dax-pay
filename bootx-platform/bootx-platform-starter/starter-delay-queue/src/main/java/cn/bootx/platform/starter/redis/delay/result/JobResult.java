package cn.bootx.platform.starter.redis.delay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 延时任务
 * @author xxm
 * @since 2024/9/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "延时任务")
public class JobResult {

    @Schema(description = "任务id")
    private String jobId;

    @Schema(description = "主题")
    private String topic;

    @Schema(description = "延时时间")
    private LocalDateTime delayTime;
}
