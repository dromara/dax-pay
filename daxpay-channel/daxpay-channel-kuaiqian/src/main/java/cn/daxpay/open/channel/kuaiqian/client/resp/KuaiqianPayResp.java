package cn.daxpay.open.channel.kuaiqian.client.resp;

import cn.daxpay.open.channel.kuaiqian.client.enums.KuaiqianPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 快钱通道支付响应(主应用侧)
///
/// 子应用调用快钱 MAS 网关后回传。当面付聚合扫码恒为异步支付(complete=false)。
@Data
@Accessors(chain = true)
public class KuaiqianPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 支付内容(聚合二维码链接 qrCode)
    private String payBody;

    /// 支付内容类型(恒 QR_CODE)
    private KuaiqianPayBodyType payBodyType;

    /// 是否已终态完成(恒 false, 等待异步通知)
    private Boolean complete;

    /// 快钱订单控制号(idOrderCtrl, 若下单响应携带则回传)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
