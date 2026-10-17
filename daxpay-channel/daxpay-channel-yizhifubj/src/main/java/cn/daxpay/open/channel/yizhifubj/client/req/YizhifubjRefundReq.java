package cn.daxpay.open.channel.yizhifubj.client.req;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 一直付通道退款请求(主应用侧)
///
/// 走 5upay 网关 `/onlinePay/refund`, 按原订单通道流水号(orderId = serialNumber)原路退回, 同步受理。
@Data
public class YizhifubjRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private YizhifubjSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为退款 requestId 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原订单通道流水号(serialNumber, 退款 orderId)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款异步通知地址(可空, 退款结果以同步响应为准)
    private String notifyUrl;
}
