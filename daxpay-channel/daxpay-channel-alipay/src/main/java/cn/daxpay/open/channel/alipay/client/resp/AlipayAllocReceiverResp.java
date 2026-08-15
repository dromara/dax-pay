package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝通道分账接收方绑定响应(绑定/解绑共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAllocReceiverResp` 镜像, 字段对齐。
/// 绑定/解绑成功无业务数据, 失败时填充错误码与错误信息。
@Data
@Accessors(chain = true)
public class AlipayAllocReceiverResp {

    /// 错误码(业务失败时, 支付宝 subCode)
    private String errorCode;

    /// 错误信息(业务失败时, 支付宝 subMsg)
    private String errorMsg;
}
