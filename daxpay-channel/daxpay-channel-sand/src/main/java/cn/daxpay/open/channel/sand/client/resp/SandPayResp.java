package cn.daxpay.open.channel.sand.client.resp;

import cn.daxpay.open.channel.sand.client.enums.SandPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 杉德通道支付响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class SandPayResp {

    /// 商户订单号(透传)
    private String outTradeNo;

    /// 杉德订单流水号(sandSerialNo)
    private String tradeNo;

    /// 支付内容(扫码 = credential.qrCode; 微信 JSAPI = credential 调起参数 JSON; 支付宝 JSAPI = credential.tradeNo)
    private String payBody;

    /// 支付内容类型
    private SandPayBodyType payBodyType;

    /// 是否已终态完成(下单恒 false)
    private Boolean complete;
}
