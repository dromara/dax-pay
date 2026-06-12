package org.dromara.daxpay.platform.common.rocketmq.message;

import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.dromara.daxpay.platform.common.rocketmq.exception.RocketmqException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

/// # RocketMQ消息转换器
///
/// 统一消息对象与JSON之间的转换，复用项目现有Jackson能力
@Slf4j
@UtilityClass
public class RocketmqMessageConverter {

    /// 将消息对象序列化为JSON字符串
    ///
    /// @param message 消息对象
    /// @return JSON字符串
    public String toJson(Object message) {
        try {
            return JacksonUtil.toJson(message, false);
        } catch (Exception e) {
            log.error("RocketMQ消息序列化失败: {}", e.getMessage(), e);
            throw new RocketmqException("error.rocketmq.serializeFailed", e.getMessage());
        }
    }

    /// 从RocketMQ原始消息中解析出消息体
    ///
    /// @param messageExt RocketMQ原始消息
    /// @param bodyClass  消息体类型
    /// @return 消息体
    public <T> T parseBody(MessageExt messageExt, Class<T> bodyClass) {
        try {
            String json = new String(messageExt.getBody());
            return JacksonUtil.toBean(json, bodyClass);
        } catch (Exception e) {
            log.error("RocketMQ消息体解析失败: {}", e.getMessage(), e);
            throw new RocketmqException("error.rocketmq.parseBodyFailed", e.getMessage());
        }
    }

    /// 对象类型转换
    /// 用于将Map、Object等对象转换为指定类型
    ///
    /// @param from 来源对象
    /// @param to   目标类型
    /// @return 目标对象
    public <T> T convert(Object from, Class<T> to) {
        try {
            return JacksonUtil.convert(from, to);
        } catch (Exception e) {
            log.error("RocketMQ消息类型转换失败: {}", e.getMessage(), e);
            throw new RocketmqException("error.rocketmq.typeConvertFailed", e.getMessage());
        }
    }
}


