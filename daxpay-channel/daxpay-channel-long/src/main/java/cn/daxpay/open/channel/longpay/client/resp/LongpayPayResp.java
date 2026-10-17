package cn.daxpay.open.channel.longpay.client.resp;

import cn.daxpay.open.channel.longpay.client.enums.LongpayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 建行龙支付通道支付响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class LongpayPayResp {

    /// 商户订单号(透传 LongpayPayReq.outTradeNo)
    private String outTradeNo;

    /// 支付内容(聚合二维码码串 QRCODE)
    private String payBody;

    /// 支付内容类型(恒 QR_CODE)
    private LongpayPayBodyType payBodyType;

    /// 是否已终态完成(聚合下单恒 false)
    private Boolean complete;

    /// 订单总金额(单位: 分)
    private Long totalAmount;

    /// 原始响应报文(用于对账/排障)
    private String syncData;
}
