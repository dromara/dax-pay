package cn.daxpay.open.channel.adapay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 汇付天下通道关闭订单响应(主应用侧)
@Data
@Accessors(chain = true)
public class AdapayCloseResp {

    /// 商户订单号(回显)
    private String outTradeNo;
}
