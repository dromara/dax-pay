package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;

/// # 支付宝通道关闭响应
///
/// 与子应用 dax-pay-channel-one 的 `AlipayCloseResp` 镜像, 字段对齐。
/// 关闭失败且可兜底(交易已关闭/交易不存在)时由子应用内部消化为成功;
/// 真正失败时子应用抛 [cn.daxpay.open.platform.core.exception.ChannelServiceException](经 DaxResult 透传)。
@Data
public class AlipayCloseResp {

    /// 商户订单号(透传 AlipayCloseReq.outTradeNo)
    private String outTradeNo;

    /// 支付宝交易号(trade_no)
    private String tradeNo;

    /// 网关返回码(code, 10000=成功)
    private String code;

    /// 业务返回码(sub_code, 如 ACQ.TRADE_NOT_EXIST)
    private String subCode;

    /// 业务返回消息(sub_msg)
    private String subMsg;
}
