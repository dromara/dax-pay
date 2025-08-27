package org.dromara.daxpay.channel.alipay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 支付宝服务商配置参数
 * @author xxm
 * @since 2024/10/31
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商配置参数")
public class AlipayIsvConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 支付宝商户appId */
    @NotBlank(message = "支付宝AppId不可为空")
    @Schema(description = "支付宝商户appId")
    private String aliAppId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    @NotNull(message = "是否启用不可为空")
    private Boolean enable;

    /**
     * 认证类型 证书/公钥
     * @see AlipayCode.AuthType
     */
    @Schema(description = "认证类型")
    @NotBlank(message = "认证类型不可为空")
    private String authType;

    /** 签名类型 RSA2 */
    @Schema(description = "签名类型")
    @NotBlank(message = "签名类型不可为空")
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @Schema(description = "支付宝商家唯一识别码")
    private String alipayUserId;

    /** 支付宝公钥 */
    @Schema(description = "支付宝公钥")
    public String alipayPublicKey;

    /** 私钥 */
    @Schema(description = "私钥")
    private String privateKey;

    /** 应用公钥证书 */
    @Schema(description = "应用公钥证书")
    private String appCert;

    /** 支付宝公钥证书 */
    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    /** 支付宝CA根证书 */
    @Schema(description = "支付宝CA根证书")
    private String alipayRootCert;

    /** 是否沙箱环境 */
    @Schema(description = "是否沙箱环境")
    private boolean sandbox;


}

