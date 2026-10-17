package cn.daxpay.open.channel.epaylinks.client.resp;

import cn.daxpay.open.channel.epaylinks.client.enums.EpaylinksPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易票联通道支付响应(主应用侧)
@Data
@Accessors(chain = true)
public class EpaylinksPayResp {

    /// 商户订单号(透传 EpaylinksPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(扫码族=二维码 / JSAPI 族=调起串 / 收银台=跳转链接)
    private String payBody;

    /// 支付内容类型
    private EpaylinksPayBodyType payBodyType;

    /// 是否已终态完成(易票联下单恒 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;
}
