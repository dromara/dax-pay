package cn.daxpay.open.channel.xingyi.client.req;

import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 星驿付通道退款请求(主应用侧)
///
/// 走 `/yyfsevr/order/refund`, 按原星驿付订单号原路退回, 退款为同步受理即成功(无退款异步通知)。
@Data
public class XingyiRefundReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private XingyiSdkCredential credential;

    /// 商户退款单号(主应用退款关联号, 作为 orderNo 上送)
    @NotBlank(message = "{validation.field.outRefundNo.notBlank}")
    private String outRefundNo;

    /// 原星驿付订单号(原支付下单响应/回调获得)
    @NotBlank(message = "{validation.field.tradeNo.notBlank}")
    private String originOutOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    private Long amount;

    /// 退款标记(原支付渠道类型, 对齐参考插件 tag 参数: wxpay/alipay/bank)
    private String tag;
}
