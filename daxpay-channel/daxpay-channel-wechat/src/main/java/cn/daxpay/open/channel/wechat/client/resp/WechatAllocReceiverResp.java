package cn.daxpay.open.channel.wechat.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信通道分账接收方绑定响应(绑定/解绑共用)
///
/// 与子应用 dax-pay-channel-one 的 `WechatAllocReceiverResp` 镜像, 字段对齐。
/// 绑定/解绑成功无业务数据, 失败时填充错误码与错误信息。
@Data
@Accessors(chain = true)
public class WechatAllocReceiverResp {

    /// 错误码(业务失败时)
    private String errorCode;

    /// 错误信息(业务失败时)
    private String errorMsg;
}
