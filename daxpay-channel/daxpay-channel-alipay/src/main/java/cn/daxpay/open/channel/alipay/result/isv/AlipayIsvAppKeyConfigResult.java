package cn.daxpay.open.channel.alipay.result.isv;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用密钥配置
///
/// 支付宝服务商应用密钥和证书的返回结果对象，脱敏展示敏感字段。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用密钥配置")
public class AlipayIsvAppKeyConfigResult extends BaseResult {

    /// 支付宝服务商应用ID
    @Schema(description = "应用ID")
    private Long alipayIsvAppId;

    /// 认证类型
    @Schema(description = "认证类型")
    private String authType;

    /// 支付宝公钥
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;

    /// 应用私钥
    @Schema(description = "应用私钥")
    private String privateKey;

    /// 应用公钥证书
    @Schema(description = "应用公钥证书")
    private String appCert;

    /// 支付宝公钥证书
    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    /// 支付宝CA根证书
    @Schema(description = "支付宝CA根证书")
    private String alipayRootCert;

    /// AES通信密钥
    @Schema(description = "AES通信密钥")
    private String secretKey;
}
