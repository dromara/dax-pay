package cn.daxpay.open.platform.system.result.config.platform;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台支付宝开放平台认证配置
///
/// 敏感字段使用 [@SensitiveInfo] 注解, 返回时由 Jackson 序列化器脱敏。
///
@Data
@Accessors(chain = true)
@Schema(title = "平台支付宝开放平台认证配置")
public class PlatformAlipayAuthConfigResult {

    /// 支付宝开放平台应用 appId
    @Schema(description = "支付宝开放平台应用 appId")
    private String appId;

    /// 鉴权方式: public_key(公钥模式) / cert(证书模式)
    @Schema(description = "鉴权方式: public_key(公钥) / cert(证书)")
    private String authType;

    /// 应用私钥(脱敏返回)
    @SensitiveInfo
    @Schema(description = "应用私钥")
    private String privateKey;

    /// 支付宝公钥(脱敏返回)
    @SensitiveInfo
    @Schema(description = "支付宝公钥(公钥模式)")
    private String alipayPublicKey;

    /// 应用公钥证书内容(脱敏返回)
    @SensitiveInfo
    @Schema(description = "应用公钥证书内容(证书模式)")
    private String appCert;

    /// 支付宝公钥证书内容(脱敏返回)
    @SensitiveInfo
    @Schema(description = "支付宝公钥证书内容(证书模式)")
    private String alipayCert;

    /// 支付宝根证书内容(脱敏返回)
    @SensitiveInfo
    @Schema(description = "支付宝根证书内容(证书模式)")
    private String alipayRootCert;

    /// 是否沙箱环境
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
