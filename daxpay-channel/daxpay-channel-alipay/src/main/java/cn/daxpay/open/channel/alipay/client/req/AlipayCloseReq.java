package cn.daxpay.open.channel.alipay.client.req;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import lombok.Data;

/// # 支付宝通道关闭请求
///
/// 与子应用 dax-pay-channel-one 的 `AlipayCloseReq` 镜像, 经声明式 HTTP 客户端序列化传输, 字段对齐。
@Data
public class AlipayCloseReq {

    /// 商户订单号(主应用支付交易号, 作为支付宝 out_trade_no)
    private String outTradeNo;

    /// 支付宝交易号(trade_no, 下单成功后由支付宝返回, 可选)
    private String tradeNo;

    /// 是否使用撤销方式关闭订单(false=交易关闭, true=交易撤销)
    private boolean useCancel;

    /// 通道调用凭证
    private AlipaySdkCredential credential;
}
