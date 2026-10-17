package cn.daxpay.open.channel.baofu.client.resp;

import cn.daxpay.open.channel.baofu.client.enums.BaofuPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 宝付通道支付响应(主应用侧)
///
/// 子应用调用宝付聚合网关后回传。下单均为异步支付(complete=false, 含付款码被扫), 等待异步通知确认。
@Data
@Accessors(chain = true)
public class BaofuPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 支付内容(二维码链接 / JSAPI 调起参数 / 渠道交易号标识 / 跳转链接; 付款码场景为空)
    private String payBody;

    /// 支付内容类型(付款码场景为空)
    private BaofuPayBodyType payBodyType;

    /// 是否已终态完成(恒 false, 等待异步通知)
    private Boolean complete;

    /// 宝付交易号(tradeNo, 若下单响应携带则回传)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
