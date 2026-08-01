package cn.daxpay.open.channel.union.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 云闪付通道关闭订单响应
@Data
@Accessors(chain = true)
public class UnionCloseResp {

    /// 商户订单号(回显)
    private String outTradeNo;
}
