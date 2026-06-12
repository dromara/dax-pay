package org.dromara.daxpay.platform.common.rocketmq.service;

import org.dromara.daxpay.platform.common.rocketmq.exception.RocketmqException;
import org.dromara.daxpay.platform.common.rocketmq.message.RocketmqMessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

/// # RocketMQ统一发送服务
///
/// 封装RocketMQ发送能力，提供统一的发送入口
@Slf4j
@Service
@RequiredArgsConstructor
public class RocketmqTemplateService {

    private final RocketMQTemplate rocketMQTemplate;

    /// 同步发送消息
    ///
    /// @param topic 消息主题，消息的第一级分类
    /// @param tag   消息标签，消息的第二级分类，用于消息过滤
    /// @param body  消息体
    /// @return 发送结果
    public <T> SendResult send(String topic, String tag, T body) {
        String keys = UUID.randomUUID().toString(true);
        String json = RocketmqMessageConverter.toJson(body);
        String destination = buildDestination(topic, tag);
        Message<String> msg = MessageBuilder.withPayload(json)
                .setHeader(RocketMQHeaders.KEYS, keys)
                .build();

        try {
            SendResult sendResult = rocketMQTemplate.syncSend(destination, msg);
            if (!sendResult.getSendStatus().name().equals("SEND_OK")) {
                throw new RocketmqException("error.rocketmq.sendFailed", sendResult.getSendStatus());
            }
            log.debug("消息发送成功: keys={}, msgId={}, body={}", keys, sendResult.getMsgId(), json);
            return sendResult;
        } catch (Exception e) {
            log.error("消息发送异常: keys={}, error={}", keys, e.getMessage(), e);
            throw new RocketmqException("error.rocketmq.sendFailed", e.getMessage());
        }
    }

    /// 发送延时消息（自定义延时时间，RocketMQ 5.x）
    ///
    /// 延时时间最大支持24小时
    ///
    /// @param topic        消息主题，消息的第一级分类
    /// @param tag          消息标签，消息的第二级分类，用于消息过滤
    /// @param body         消息体
    /// @param delaySeconds 延时秒数
    /// @return 发送结果
    public <T> SendResult sendDelay(String topic, String tag, T body, int delaySeconds) {
        String keys = UUID.randomUUID().toString(true);
        String json = RocketmqMessageConverter.toJson(body);
        String destination = buildDestination(topic, tag);
        Message<String> msg = MessageBuilder.withPayload(json)
                .setHeader(RocketMQHeaders.KEYS, keys)
                .build();
        try {
            SendResult sendResult = rocketMQTemplate.syncSendDelayTimeSeconds(destination, msg, delaySeconds);
            if (!sendResult.getSendStatus().name().equals("SEND_OK")) {
                throw new RocketmqException("error.rocketmq.delaySendFailed", sendResult.getSendStatus());
            }
            log.info("延时消息发送成功: keys={}, msgId={}, body={}", keys, sendResult.getMsgId(), json);
            return sendResult;
        } catch (Exception e) {
            log.error("延时消息发送异常: keys={}, error={}", keys, e.getMessage(), e);
            throw new RocketmqException("error.rocketmq.delaySendFailed", e.getMessage());
        }
    }

    /// 构建目的地字符串
    ///
    /// RocketMQTemplate 需要通过 destination 参数传递 topic 和 tag，
    /// 格式为 "topic:tag"，否则 tag 不会生效
    ///
    /// @param topic 消息主题
    /// @param tag   消息标签，为空时只返回 topic
    /// @return 目的地字符串，格式为 "topic" 或 "topic:tag"
    private String buildDestination(String topic, String tag) {
        if (StrUtil.isNotEmpty(tag)) {
            return topic + ":" + tag;
        }
        return topic;
    }
}


