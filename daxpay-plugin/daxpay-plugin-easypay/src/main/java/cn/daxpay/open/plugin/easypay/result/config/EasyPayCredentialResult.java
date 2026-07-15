package cn.daxpay.open.plugin.easypay.result.config;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易支付凭证结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付凭证结果")
public class EasyPayCredentialResult extends MchBaseResult {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 启用
    @Schema(description = "启用")
    private Boolean enable;

    /// 开启 V1
    @Schema(description = "开启V1")
    private Boolean enableV1;

    /// 开启 V2
    @Schema(description = "开启V2")
    private Boolean enableV2;

    /// V1 MD5 密钥
    @Schema(description = "MD5密钥")
    private String md5Key;

    /// V2 使用系统公私钥
    @Schema(description = "使用系统密钥")
    private Boolean useSystemKey;

    /// 商户 RSA 公钥
    @Schema(description = "商户RSA公钥")
    private String publicKey;

    /// 平台 RSA 公钥（纯 Base64 展示）
    @Schema(description = "平台RSA公钥")
    private String platformPublicKey;

    /// V1 对接地址
    @Schema(description = "V1对接地址")
    private String easyPayV1ApiUrl;

    /// V2 对接地址
    @Schema(description = "V2对接地址")
    private String easyPayV2ApiUrl;
}
