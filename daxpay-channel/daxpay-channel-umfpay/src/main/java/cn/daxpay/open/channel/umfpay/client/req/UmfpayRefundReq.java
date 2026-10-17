package cn.daxpay.open.channel.umfpay.client.req;

import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 联动优势通道退款请求(主应用侧)
///
/// 走老网关 mer_refund 接口, 按原商户单号 + 原下单日期原路退回, 同步终态。
@Data
public class UmfpayRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private UmfpaySdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 refund_no 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原商户订单号(原支付下单时的 order_id, 即主平台支付交易号)
    @NotBlank(message = "{validation.field.originOutTradeNo.notBlank}")
    private String originOutTradeNo;

    /// 原下单日期(yyyyMMdd 东八区, 联动优势按 order_id+mer_date 定位原订单; 由主应用反查原支付单生成时间转换)
    private String originMerDate;

    /// 原订单总金额(单位: 分, org_amount)
    @NotNull(message = "{validation.field.totalAmount.notNull}")
    @Positive(message = "{validation.field.totalAmount.positive}")
    private Long totalAmount;

    /// 退款金额(单位: 分, refund_amount)
    @NotNull(message = "{validation.field.refundAmount.notNull}")
    @Positive(message = "{validation.field.refundAmount.positive}")
    private Long refundAmount;

    /// 退款原因(备用字段, mer_refund 不上送)
    private String reason;
}
