package cn.daxpay.open.channel.fubei.client.resp;

import cn.daxpay.open.channel.fubei.client.enums.FubeiPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 付呗通道支付响应(主应用侧)
///
/// 子应用调用付呗网关后回传给主应用。
/// 扫码/JSAPI/H5 族下单均为异步支付(complete=false), 等待异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class FubeiPayResp {

    /// 商户订单号(透传 FubeiPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(云闪付=qr_code / 微信=sign_package JSON / 支付宝JS=prepay_id / H5=表单HTML或跳转URL)
    private String payBody;

    /// 支付内容类型
    private FubeiPayBodyType payBodyType;

    /// 是否已终态完成(付呗下单恒 false, 等待异步通知)
    private Boolean complete;

    /// 付呗订单号(order_sn, 供后续查单/退款)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
