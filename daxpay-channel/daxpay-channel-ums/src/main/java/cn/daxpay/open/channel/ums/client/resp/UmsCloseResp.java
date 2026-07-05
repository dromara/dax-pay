package cn.daxpay.open.channel.ums.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 银联商务通道关闭订单响应
@Data
@Accessors(chain = true)
public class UmsCloseResp {

    /// 商户订单号(回显)
    private String outTradeNo;
}
