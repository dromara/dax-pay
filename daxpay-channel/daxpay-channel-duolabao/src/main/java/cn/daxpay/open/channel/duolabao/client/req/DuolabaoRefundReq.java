package cn.daxpay.open.channel.duolabao.client.req;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 哆啦宝通道退款请求(主应用侧)
///
/// 走 `/api/refundByRequestNum`: 按原商户请求单号原路退回, 同步受理即成功。
@Data
public class DuolabaoRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private DuolabaoSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 refundRequestNum 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付下单时的 requestNum, 即主平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 退款金额(单位: 分, 支持部分退款)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款原因
    private String reason;
}
