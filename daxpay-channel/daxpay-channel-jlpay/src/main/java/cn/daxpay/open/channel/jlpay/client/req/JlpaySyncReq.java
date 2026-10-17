package cn.daxpay.open.channel.jlpay.client.req;

import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 嘉联通道订单同步请求
///
/// 走 /open/trans/chnquery 订单查询, 按商户订单号(out_trade_no)查询,
/// 嘉联交易号(transaction_id)作为备选查询条件(二选一, 有则优先)。
@Data
public class JlpaySyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private JlpaySdkCredential credential;

    /// 商户订单号(主应用支付交易号, 上送 out_trade_no)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;

    /// 嘉联交易号(transaction_id, 可空; 有则上送由嘉联直查)
    private String outOrderNo;
}
