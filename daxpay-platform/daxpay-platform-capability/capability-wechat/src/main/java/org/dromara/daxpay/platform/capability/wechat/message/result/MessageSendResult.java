package org.dromara.daxpay.platform.capability.wechat.message.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 消息发送结果
///
@Data
@Accessors(chain = true)
public class MessageSendResult {

    /// 是否成功
    private Boolean success;

    /// 微信消息ID
    private String msgId;

    /// 错误码
    private String errorCode;

    /// 错误信息
    private String errorMsg;

    /// 消息记录ID
    private Long recordId;
}
