package org.dromara.daxpay.channel.alipay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付宝配置参数
 * @author xxm
 * @since 2024/6/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝配置参数")
public class AlipayConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 是否为ISV商户(特约商户) */
    @Schema(description = "是否为ISV商户(特约商户)")
    private boolean isv;

    /** 支付宝特约商户Token */
    @Schema(description = "支付宝特约商户Token")
    private String appAuthToken;

    /** 支付宝商户appId */
    @NotBlank(message = "支付宝AppId不可为空")
    @Schema(description = "支付宝商户appId")
    private String aliAppId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    @NotBlank(message = "是否启用不可为空")
    private Boolean enable;

    /** 支付限额 */
    @Schema(description = "支付限额")
    @NotBlank(message = "支付限额不可为空")
    private BigDecimal limitAmount;

    /**
     * 认证类型 证书/公钥
     * @see org.dromara.daxpay.channel.alipay.code.AlipayCode.AuthType
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

    /** 商户AppId */
    @Schema(description = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;
}
