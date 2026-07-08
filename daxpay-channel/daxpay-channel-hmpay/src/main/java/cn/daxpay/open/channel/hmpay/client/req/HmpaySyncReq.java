package cn.daxpay.open.channel.hmpay.client.req;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import lombok.Data;

/// # 河马付通道订单同步请求(主应用侧)
@Data
public class HmpaySyncReq {

    /// 通道调用凭证
    private HmpaySdkCredential credential;

    /// 原商户订单号
    private String outTradeNo;

    /// 原支付下单时间(yyyyMMddHHmmss)
    private String orderCreateTime;
}
