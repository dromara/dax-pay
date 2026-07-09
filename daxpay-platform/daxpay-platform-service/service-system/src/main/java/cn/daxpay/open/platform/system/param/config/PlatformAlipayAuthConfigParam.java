package cn.daxpay.open.platform.system.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台支付宝开放平台认证配置参数
///
/// 敏感字段(privateKey/证书内容) 编辑时未修改由前端不传字段(undefined) + 默认 NOT_NULL 策略跳过更新,
/// 详见 [PlatformAlipayAuthConfigService#updateAlipayAuthConfig]。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台支付宝开放平台认证配置参数")
public class PlatformAlipayAuthConfigParam {

    /// 支付宝开放平台应用 appId
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "支付宝开放平台应用 appId")
    private String appId;

    /// 鉴权方式: public_key(公钥模式) / cert(证书模式)
    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Schema(description = "鉴权方式: public_key(公钥) / cert(证书)")
    private String authType;

    /// 应用私钥
    @Schema(description = "应用私钥")
    private String privateKey;

    /// 支付宝公钥(公钥模式)
    @Schema(description = "支付宝公钥(公钥模式)")
    private String alipayPublicKey;

    /// 应用公钥证书内容(证书模式)
    @Schema(description = "应用公钥证书内容(证书模式)")
    private String appCert;

    /// 支付宝公钥证书内容(证书模式)
    @Schema(description = "支付宝公钥证书内容(证书模式)")
    private String alipayCert;

    /// 支付宝根证书内容(证书模式)
    @Schema(description = "支付宝根证书内容(证书模式)")
    private String alipayRootCert;

    /// 是否沙箱环境
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
