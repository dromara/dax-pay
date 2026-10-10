package cn.daxpay.open.payment.auth.develop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证调试 - 通道授权链接参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext]
/// 取当前登录商户组装, 对照 [DevelopChannelAuthParam])。
///
/// 直接选择应用模式: 显式传 scope + appId, 由 [DevelopAuthService] 精确加载后转换为
/// [cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam] 传给
/// [cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService]。
@Data
@Accessors(chain = true)
@Schema(title = "认证调试通道授权参数(商户端)")
public class MchDevelopChannelAuthParam {

    /// 应用档位(必填, platform 平台档 / merchant 商户档)
    @NotBlank(message = "{validation.field.scope.notBlank}")
    @Schema(description = "应用档位(platform/merchant)")
    private String scope;

    /// 应用主键(必填, 对应 wx_platform_app/wx_mch_app 或 dy_platform_app/dy_mch_app 的主键)
    @NotNull(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用主键")
    private Long appId;
}
