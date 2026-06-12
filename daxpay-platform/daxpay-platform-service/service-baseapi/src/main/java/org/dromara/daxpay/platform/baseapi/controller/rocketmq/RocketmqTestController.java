package org.dromara.daxpay.platform.baseapi.controller.rocketmq;

import org.dromara.daxpay.platform.common.rocketmq.service.RocketmqTemplateService;
import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.baseapi.entity.rocketmq.RocketmqTestMessage;
import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # RocketMQ测试控制器
///
@Tag(name = "RocketMQ测试")
@RestController
@RequestMapping("/test/rocketmq")
@RequiredArgsConstructor
public class RocketmqTestController {

    private final RocketmqTemplateService rocketmqTemplateService;

    private static final String TOPIC = "test-topic";
    private static final String TAG_NORMAL = "test-tag";
    private static final String TAG_DELAY = "delay-tag";

    @Operation(summary = "发送同步消息")
    @PostMapping("/send")
    public Result<String> send(@RequestBody RocketmqTestMessage message) {
        SendResult result = rocketmqTemplateService.send(TOPIC, TAG_NORMAL, message);
        return Res.ok("消息发送成功: " + result.getMsgId());
    }

    @Operation(summary = "发送延迟消息(5秒)")
    @PostMapping("/send-delay")
    public Result<String> sendDelay(@RequestBody RocketmqTestMessage message) {
        var result = rocketmqTemplateService.sendDelay(TOPIC, TAG_DELAY, message, 5);
        return Res.ok("延迟消息发送成功: " + result.getMessageQueue());
    }
}
