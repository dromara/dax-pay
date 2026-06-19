package cn.daxpay.open.demo.artemis.result;
import cn.daxpay.open.demo.artemis.model.DemoArtemisMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Artemis 演示消息消费记录
///
/// 消费者处理一条消息后生成，存入内存供前端轮询展示。
///
@Data
@Accessors(chain = true)
@Schema(title = "Artemis 演示消息消费记录")
public class DemoMessageResult {

    /// 业务消息 ID
    @Schema(description = "业务消息ID")
    private String id;

    /// 消息场景（QUEUE / TOPIC / DELAY）
    @Schema(description = "消息场景")
    private String scene;

    /// 消息内容
    @Schema(description = "消息内容")
    private String content;

    /// 发送时间（UTC）
    @Schema(description = "发送时间(UTC)")
    private OffsetDateTime sendTime;

    /// 消费时间（UTC）
    @Schema(description = "消费时间(UTC)")
    private OffsetDateTime consumeTime;

    /// 发送到消费的耗时（毫秒）
    @Schema(description = "端到端耗时(毫秒)")
    private long costMillis;

    /// 消费者名称
    @Schema(description = "消费者名称")
    private String consumer;

    /// 从消息体构造消费记录，自动填充消费时间与耗时
    public static DemoMessageResult from(DemoArtemisMessage message, String consumer) {
        OffsetDateTime now = OffsetDateTime.now();
        DemoMessageResult result = new DemoMessageResult()
                .setId(message.getId())
                .setScene(message.getScene())
                .setContent(message.getContent())
                .setSendTime(message.getSendTime())
                .setConsumeTime(now)
                .setConsumer(consumer);
        if (message.getSendTime() != null) {
            long cost = now.toInstant().toEpochMilli() - message.getSendTime().toInstant().toEpochMilli();
            // 处理时钟回拨导致的负值
            result.setCostMillis(Math.max(0, cost));
        }
        return result;
    }
}
