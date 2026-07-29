package cn.daxpay.open.payment.auth.develop;

import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证调试 - 微信支付授权链接参数
///
/// 调试专用, 不继承支付公共参数(无 reqTime/sign 等字段)。
/// 直接选择应用模式: 显式传 scope + appId, 由 [DevelopAuthService] 精确加载后转换为
/// [GenerateAuthUrlParam] 传给 [MerchantChannelAuthService]。
@Data
@Accessors(chain = true)
@Schema(title = "认证调试微信支付授权参数")
public class DevelopChannelAuthParam {

    /// 商户号(必填, 初始化商户上下文)
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用档位(必填, platform 平台档 / merchant 商户档)
    @NotBlank(message = "{validation.field.scope.notBlank}")
    @Schema(description = "应用档位(platform/merchant)")
    private String scope;

    /// 应用主键(必填, 对应 wx_platform_app.id 或 wx_mch_app.id)
    @NotNull(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用主键")
    private Long appId;
}