package cn.daxpay.open.channel.youzan.client.resp;

import cn.daxpay.open.channel.youzan.client.enums.YouzanPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 有赞通道支付响应
///
/// 子应用调用有赞开放网关统一下单后回传给主应用。
/// 有赞 JS/H5 类支付均为异步支付(complete=false), 等待 pay_notify_url 异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class YouzanPayResp {

    /// 商户订单号(透传 YouzanPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(JS/小程序为 deeplink 调起参数, 支付宝 JS 为 deeplink 标识码, H5 为 awake_pay_url 唤醒链接)
    private String payBody;

    /// 支付内容类型(JSAPI / IDENTIFIER / LINK)
    private YouzanPayBodyType payBodyType;

    /// 是否已终态完成(JS/H5 类下单恒 false)
    private Boolean complete;

    /// 有赞订单号(trade_no, 供退款/对账使用)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
