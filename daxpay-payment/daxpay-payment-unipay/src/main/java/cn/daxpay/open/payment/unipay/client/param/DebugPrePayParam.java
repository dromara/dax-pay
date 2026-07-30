package cn.daxpay.open.payment.unipay.client.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 调试预下单参数(免签名, 仅沙箱环境)
///
/// 与正式 [cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam] 相比,
/// 省略商户签名字段(sign/reqId/nonceStr 等), 仅供收银台小程序调试中心联调使用。
/// 受 [DebugGatewayController] 的 `@ConditionalOnProperty(sandbox-enabled=true)` 保护,
/// 生产环境(sandbox-enabled=false)整个 Controller 不注册, 本类不会被使用。
@Data
@Schema(title = "调试预下单参数")
public class DebugPrePayParam {

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    @Schema(description = "应用ID")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    @Schema(description = "商户订单号(可空, 空则后端自动生成调试单号)")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    @Schema(description = "支付金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    @Max(value = 9999999999L, message = "{validation.field.amount.max}")
    private Long amount;

    @Schema(description = "支付标题")
    @NotBlank(message = "{validation.field.title.notBlank}")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    /// @see cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum
    @Schema(description = "网关支付类型 cashier/aggregate")
    @NotBlank(message = "{validation.field.gatewayPayType.notBlank}")
    @Size(max = 32, message = "{validation.field.gatewayPayType.size}")
    private String gatewayPayType;
}
