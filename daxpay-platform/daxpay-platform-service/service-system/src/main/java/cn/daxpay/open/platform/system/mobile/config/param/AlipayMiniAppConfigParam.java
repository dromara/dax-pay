package cn.daxpay.open.platform.system.mobile.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝小程序应用配置入参
///
/// 敏感字段为空表示不更新; 公钥/证书材料互斥校验在 Service 按新建/更新处理。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝小程序应用配置参数")
public class AlipayMiniAppConfigParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "小程序 AppId")
    private String appId;

    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Schema(description = "鉴权方式: public_key / cert")
    private String authType;

    @Schema(description = "应用私钥(空则不更新)")
    private String privateKey;

    @Schema(description = "支付宝公钥(公钥模式, 空则不更新)")
    private String alipayPublicKey;

    @Schema(description = "应用公钥证书(证书模式, 空则不更新)")
    private String appCert;

    @Schema(description = "支付宝公钥证书(证书模式, 空则不更新)")
    private String alipayCert;

    @Schema(description = "支付宝根证书(证书模式, 空则不更新)")
    private String alipayRootCert;
}
