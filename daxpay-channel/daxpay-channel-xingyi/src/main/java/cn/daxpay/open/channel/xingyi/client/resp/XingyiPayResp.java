package cn.daxpay.open.channel.xingyi.client.resp;

import cn.daxpay.open.channel.xingyi.client.enums.XingyiPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 星驿付通道支付响应(主应用侧)
///
/// 子应用调用星驿付网关后回传。扫码族与 JSAPI 族为异步受理(complete=false),
/// 付款码为同步交易(complete=支付成功时 true)。
@Data
@Accessors(chain = true)
public class XingyiPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 星驿付订单号(下单响应携带, 供后续查单/退款; 缺省时经回调补充)
    private String outOrderNo;

    /// 支付内容(二维码链接 / JSAPI 调起参数 JSON / 支付宝 prepayid 交易串)
    private String payBody;

    /// 支付内容类型(QR_CODE / JSAPI)
    private XingyiPayBodyType payBodyType;

    /// 是否已终态完成(付款码同步交易成功时 true, 其余恒 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
