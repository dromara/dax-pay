package cn.daxpay.open.channel.llianpay.client.req;

import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 连连通道订单同步请求(主应用侧)
///
/// 走连连统收单订单查询, 按商户单号(txn_seqno = 平台支付交易号)查询。
@Data
public class LlianpaySyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private LlianpaySdkCredential credential;

    /// 商户订单号(支付下单时的 txn_seqno = 平台支付交易号)
    @NotBlank(message = "{validation.field.outTradeNo.notBlank}")
    private String outTradeNo;
}
