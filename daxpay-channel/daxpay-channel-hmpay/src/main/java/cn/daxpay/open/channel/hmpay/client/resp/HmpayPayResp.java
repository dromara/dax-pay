package cn.daxpay.open.channel.hmpay.client.resp;

import cn.daxpay.open.channel.hmpay.client.enums.HmpayPayBodyType;
import lombok.Data;

/// # 河马付通道支付响应(主应用侧)
@Data
public class HmpayPayResp {

    /// 商户订单号
    private String outTradeNo;

    /// 杉德流水号(plat_trx_no)
    private String tradeNo;

    /// 支付内容
    private String payBody;

    /// 支付内容类型
    private HmpayPayBodyType payBodyType;

    /// 是否已终态完成(条码支付可能同步完成)
    private Boolean complete;
}
