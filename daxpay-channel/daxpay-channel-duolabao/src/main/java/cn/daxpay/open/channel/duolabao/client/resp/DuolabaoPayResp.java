package cn.daxpay.open.channel.duolabao.client.resp;

import cn.daxpay.open.channel.duolabao.client.enums.DuolabaoPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 哆啦宝通道支付响应(主应用侧)
///
/// 子应用调用哆啦宝网关后回传。聚合扫码与 JS 支付下单均为异步受理(complete=false)。
@Data
@Accessors(chain = true)
public class DuolabaoPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 支付内容(聚合码链接 / 跳转链接 / JS 调起参数)
    private String payBody;

    /// 支付内容类型(QR_CODE / LINK / JSAPI)
    private DuolabaoPayBodyType payBodyType;

    /// 是否已终态完成(下单恒 false, 等待异步通知)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
