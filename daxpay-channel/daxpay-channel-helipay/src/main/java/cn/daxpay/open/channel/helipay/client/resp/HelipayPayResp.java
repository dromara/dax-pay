package cn.daxpay.open.channel.helipay.client.resp;

import cn.daxpay.open.channel.helipay.client.enums.HelipayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 合利宝通道支付响应(子应用 → 主应用)
///
/// 合利宝下单为异步确认模式, 同步不返回终态, `complete` 恒为 false,
/// 最终状态由异步通知或轮询同步确认。
@Data
@Accessors(chain = true)
public class HelipayPayResp {

    /// 商户订单号(透传 HelipayPayReq.outTradeNo)
    private String outTradeNo;

    /// 合利宝流水号(rt6_serialNumber, 供后续查单/退款)
    private String outOrderNo;

    /// 支付内容(扫码二维码 / 公众号调起参数 / H5 跳转链接)
    private String payBody;

    /// 支付内容类型(QR_CODE / JSAPI / LINK)
    private HelipayPayBodyType payBodyType;

    /// 是否已终态成功(下单恒为 false, 等待异步通知确认)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
