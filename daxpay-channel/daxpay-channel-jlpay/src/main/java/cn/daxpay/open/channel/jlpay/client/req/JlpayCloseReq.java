package cn.daxpay.open.channel.jlpay.client.req;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 嘉联通道关单(撤销)请求
///
/// 走 /open/trans/cancel 撤销接口, 按原嘉联交易号(ori_transaction_id)撤销当日交易,
/// 撤销单号由子应用生成(独立于原商户单号)。
@Data
public class JlpayCloseReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JlpaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 用于生成撤销单号与日志关联)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 原嘉联交易号(ori_transaction_id, 撤销必填)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String outOrderNo;

    /// 客户端IP(上送 mch_create_ip)
    private String clientIp;
}
