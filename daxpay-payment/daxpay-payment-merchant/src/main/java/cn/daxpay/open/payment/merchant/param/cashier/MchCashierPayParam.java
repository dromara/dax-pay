package cn.daxpay.open.payment.merchant.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户收银被扫支付参数
///
/// PC收款精灵登录态调用, 商户身份取自 [cn.daxpay.open.payment.common.context.PaymentContext],
/// 请求体不携带 mchNo(避免越权与校验先于方法体执行的问题)。
@Data
@Accessors(chain = true)
@Schema(title = "商户收银被扫支付参数")
public class MchCashierPayParam {

    /// 商户订单号(收银端生成, 兼作支付幂等键)
    @Schema(description = "商户订单号(8~64位, 仅字母/数字/下划线/短横线)")
    @NotBlank(message = "{validation.field.clientOrderNo.notBlank}")
    @Size(min = 8, max = 64, message = "{validation.field.clientOrderNo.size}")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "{validation.field.clientOrderNo.pattern}")
    private String clientOrderNo;

    /// 支付金额(分)
    @Schema(description = "支付金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    @Max(value = 9999999999L, message = "{validation.field.amount.max}")
    private Long amount;

    /// 付款码(微信/支付宝等被扫付款码)
    @Schema(description = "付款码")
    @NotBlank(message = "{validation.field.authCode.notBlank}")
    @Size(max = 128, message = "{validation.field.authCode.size}")
    private String authCode;

    /// 支付标题(空则默认"扫码收款")
    @Schema(description = "支付标题")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    /// 应用号(空则使用商户默认应用)
    @Schema(description = "应用号")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    /// 门店号(空则走商户默认门店)
    @Schema(description = "门店号(空则走商户默认门店)")
    @Size(max = 32, message = "{validation.field.storeNo.size32}")
    private String storeNo;

    /// 终端号(不传时使用门店默认终端)
    @Schema(description = "终端号,不传时使用门店默认终端")
    @Size(max = 32, message = "{validation.field.terminalNo.size32}")
    private String terminalNo;

    /// 商户附加参数(回调原样返回)
    @Schema(description = "商户附加参数")
    @Size(max = 255, message = "{validation.field.attach.size255}")
    private String attach;

}
