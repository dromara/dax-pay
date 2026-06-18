package org.dromara.daxpay.payment.admin.controller.demo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Artemis 演示消息发送参数
///
/// 前端通过该参数选择消息场景并填写内容，后端按 scene 路由到对应 address。
///
@Data
@Accessors(chain = true)
@Schema(title = "Artemis 演示消息发送参数")
public class SendDemoMessageParam {

    /// 消息场景：QUEUE / TOPIC / DELAY
    @NotNull(message = "{validation.field.scene.notNull}")
    @Schema(description = "消息场景: QUEUE/TOPIC/DELAY")
    private SendScene scene;

    /// 消息内容
    @NotBlank(message = "{validation.field.demoContent.notBlank}")
    @Schema(description = "消息内容")
    private String content;

    /// 延时秒数（仅 DELAY 场景必填，范围 1-300）
    @Min(value = 1, message = "{validation.field.delaySeconds.min}")
    @Max(value = 300, message = "{validation.field.delaySeconds.max}")
    @Schema(description = "延时秒数(1-300)，仅 DELAY 场景必填")
    private Integer delaySeconds;

    /// # 消息场景枚举
    @Schema(description = "消息场景枚举")
    public enum SendScene {

        /// 点对点队列
        QUEUE,

        /// 发布订阅
        TOPIC,

        /// 延时消息
        DELAY
    }
}
