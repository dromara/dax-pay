package cn.daxpay.open.channel.yeepay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 易宝通道关闭订单响应(子应用 → 主应用)
@Data
@Accessors(chain = true)
public class YeepayCloseResp {

    /// 商户订单号(回显)
    private String outTradeNo;
}
