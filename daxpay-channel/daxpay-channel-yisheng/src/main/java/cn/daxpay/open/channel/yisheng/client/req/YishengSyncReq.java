package cn.daxpay.open.channel.yisheng.client.req;

import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易生通道订单同步请求
///
/// 由主应用 dax-pay-open 经声明式 HTTP 客户端转发, 走 easypay.merchant.query 按商户订单号查询。
@Data
public class YishengSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YishengSdkCredential credential;

    /// 商户订单号(下单时上送的 out_trade_no = 主应用支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
