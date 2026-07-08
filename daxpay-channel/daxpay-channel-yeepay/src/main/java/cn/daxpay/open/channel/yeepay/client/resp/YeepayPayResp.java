package cn.daxpay.open.channel.yeepay.client.resp;

import cn.daxpay.open.channel.yeepay.client.enums.YeepayPayBodyType;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易宝通道支付响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepayPayResp {

    /// 商户订单号(回显)
    private String outTradeNo;

    /// 易宝交易号(uniqueOrderNo)
    private String tradeNo;

    /// 支付内容(扫码: 二维码链接; H5: 跳转标识)
    private String payBody;

    /// 支付内容类型
    private YeepayPayBodyType payBodyType;
}
