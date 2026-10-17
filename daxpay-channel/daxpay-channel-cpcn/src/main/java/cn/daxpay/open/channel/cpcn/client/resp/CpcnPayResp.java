package cn.daxpay.open.channel.cpcn.client.resp;

import cn.daxpay.open.channel.cpcn.client.enums.CpcnPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 中金通道支付响应(主应用侧)
///
/// 子应用调用中金 5011 统一下单后回传给主应用。
/// 下单为异步支付(complete=false), 等待 NoticeURL 异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class CpcnPayResp {

    /// 商户订单号(透传 CpcnPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(被扫=二维码链接 QRCodeURL / 跳转=QRAuthCode 链接或调起参数)
    private String payBody;

    /// 支付内容类型(二维码内容 / JSAPI 调起参数 / 跳转链接)
    private CpcnPayBodyType payBodyType;

    /// 是否已终态完成(下单恒 false)
    private Boolean complete;

    /// 中金订单号(OrderNo, 若下单响应携带则回传)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
