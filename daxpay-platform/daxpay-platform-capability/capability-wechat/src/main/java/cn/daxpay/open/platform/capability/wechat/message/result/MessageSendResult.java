package cn.daxpay.open.platform.capability.wechat.message.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 消息发送结果(纯执行层返回)
///
/// 不含记录ID: 纯执行层不产生记录, 记录由上层(service-notify)在收到结果后自行写入.
/// 失败时封装错误码与原始错误信息, 不抛异常, 便于上层统一记录成功/失败.
@Data
@Accessors(chain = true)
public class MessageSendResult {

    /// 是否成功
    private Boolean success;

    /// 微信消息ID(成功时返回)
    private String msgId;

    /// 错误码(微信错误码, 失败时返回)
    private String errorCode;

    /// 错误信息(原始错误描述, 失败时返回)
    private String errorMsg;
}
