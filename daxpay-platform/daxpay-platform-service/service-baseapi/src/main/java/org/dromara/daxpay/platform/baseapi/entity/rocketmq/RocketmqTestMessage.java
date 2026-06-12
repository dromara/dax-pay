package org.dromara.daxpay.platform.baseapi.entity.rocketmq;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # RocketMQ测试消息体
///
@Data
@Accessors(chain = true)
@Schema(title = "RocketMQ测试消息体")
public class RocketmqTestMessage {

    @Schema(description = "消息内容")
    private String message;
}
