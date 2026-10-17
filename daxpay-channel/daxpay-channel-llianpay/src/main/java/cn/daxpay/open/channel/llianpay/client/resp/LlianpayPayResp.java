package cn.daxpay.open.channel.llianpay.client.resp;

import cn.daxpay.open.channel.llianpay.client.enums.LlianpayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 连连通道支付响应(主应用侧)
///
/// 连连下单恒为异步受理(complete=false), 等待异步通知确认最终状态。
@Data
@Accessors(chain = true)
public class LlianpayPayResp {

    /// 商户订单号(透传 LlianpayPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(扫码族=二维码链接 / JSAPI 族=调起串 / 收银台=跳转链接)
    private String payBody;

    /// 支付内容类型
    private LlianpayPayBodyType payBodyType;

    /// 是否已终态完成(连连下单恒 false)
    private Boolean complete;

    /// 连连平台流水号(platform_txno, 下单响应携带时回传)
    private String outOrderNo;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
