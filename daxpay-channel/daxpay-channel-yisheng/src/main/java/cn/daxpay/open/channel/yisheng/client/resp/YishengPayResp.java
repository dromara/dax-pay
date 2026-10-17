package cn.daxpay.open.channel.yisheng.client.resp;

import cn.daxpay.open.channel.yisheng.client.enums.YishengPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易生通道支付响应
///
/// 子应用调用易生网关后回传给主应用。
/// 下单为异步支付(complete=false), 等待异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class YishengPayResp {

    /// 商户订单号(透传 YishengPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(扫码 = code_url 二维码; 微信 JS = pay_info 调起参数 JSON; 支付宝 JS = alipayTradeNo)
    private String payBody;

    /// 支付内容类型(QR_CODE / JSAPI / IDENTIFIER)
    private YishengPayBodyType payBodyType;

    /// 是否已终态完成(下单恒 false)
    private Boolean complete;

    /// 易生订单号(order_no, 供查单/对账)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
