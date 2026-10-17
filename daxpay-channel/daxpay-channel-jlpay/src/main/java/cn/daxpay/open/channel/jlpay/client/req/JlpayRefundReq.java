package cn.daxpay.open.channel.jlpay.client.req;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 嘉联通道退款请求
///
/// 走 /open/trans/refund 退款接口, 按原嘉联交易号(ori_transaction_id)原路退回,
/// 嘉联退款为同步终态(受理成功即退款成功, 无退款异步通知与退款查询接口)。
@Data
public class JlpayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JlpaySdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 上送 out_trade_no)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原嘉联交易号(ori_transaction_id, 下单响应/回调获得)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分, 上送 total_fee)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 客户端IP(上送 mch_create_ip)
    private String clientIp;
}
