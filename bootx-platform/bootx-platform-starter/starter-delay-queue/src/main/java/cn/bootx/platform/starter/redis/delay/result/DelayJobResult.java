package cn.bootx.platform.starter.redis.delay.result;

import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 延时任务详情信息
 * @author xxm
 * @since 2024/9/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "延时任务详情信息")
public class DelayJobResult {
    /**
     * 延迟任务的唯一标识，用于检索任务
     */
    @Schema(description = "延迟任务的唯一标识，用于检索任务")
    private String id;

    /**
     * 任务类型（具体业务类型）
     */
    @Schema(description = "任务类型（具体业务类型）")
    private String topic;

    /**
     * 任务的执行时间
     */
    @Schema(description = "任务的执行时间")
    private LocalDateTime delayTime;

    /**
     * 再次投递时间
     */
    @Schema(description = "再次投递时间")
    private long ttrTime;

    /**
     * 任务具体的消息内容，用于处理具体业务逻辑用
     */
    @Schema(description = "任务具体的消息内容")
    private String message;

    /**
     * 重试次数
     */
    @Schema(description = "重试次数")
    private int retryCount;
    /**
     * 任务状态
     */
    @Schema(description = "任务状态")
    private String status;

    public static DelayJobResult init(DelayJob<?> job){
        return new DelayJobResult()
                .setId(job.getId())
                .setTopic(job.getTopic())
                .setDelayTime(LocalDateTimeUtil.of(job.getDelayTime()))
                .setTtrTime(job.getTtrTime())
                .setMessage(JSONUtil.toJsonStr(job.getMessage()))
                .setRetryCount(job.getRetryCount())
                .setStatus(job.getStatus().getCode());
    }
}
