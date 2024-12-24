package org.dromara.daxpay.channel.wechat.result.config;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信支付配置")
public class WechatPayConfigResult {
    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 微信商户Id */
    @Schema(description = "微信商户Id")
    private String wxMchId;

    /** 微信应用appId */
    @Schema(description = "微信应用appId")
    private String wxAppId;

    /** 子商户号 */
    @Schema(description = "子商户号")
    private String subMchId;

    /** 子应用号 */
    @Schema(description = "子应用号")
    private String subAppId;

    /** 是否为ISV商户(特约商户) */
    @Schema(description = "是否为ISV商户(特约商户)")
    private boolean isv;

    /** 授权认证地址 */
    @Schema(description = "授权认证地址")
    private String authUrl;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /**
     * 接口版本, 使用v2还是v3接口
     * @see WechatPayCode#API_V2
     */
    @Schema(description = "接口版本")
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @Schema(description = "APIv2 密钥")
    @SensitiveInfo
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @Schema(description = "APIv3 密钥")
    @SensitiveInfo
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @Schema(description = "APPID对应的接口密码，用于获取微信公众号jsapi支付时使用")
    @SensitiveInfo
    private String appSecret;

    /** 支付公钥(pub_key.pem) */
    @Schema(description = "支付公钥(pub_key.pem)")
    @SensitiveInfo
    private String publicKey;

    /** 支付公钥ID */
    @Schema(description = "支付公钥ID")
    @SensitiveInfo
    private String publicKeyId;

    /** 商户API证书(apiclient_cert.pem)base64编码 */
    @Schema(description = "商户API证书(apiclient_cert.pem)base64编码")
    @SensitiveInfo
    private String privateCert;

    /** 商户API证书私钥(apiclient_key.pem)证书base64编码 */
    @Schema(description = "商户API证书私钥(apiclient_key.pem)证书base64编码")
    @SensitiveInfo
    private String privateKey;

    /** 商户API证书序列号 */
    @Schema(description = "证书序列号")
    @SensitiveInfo
    private String certSerialNo;

    /** apiclient_cert.p12证书Base64 */
    @Schema(description = "API证书中p12证书")
    @SensitiveInfo
    private String p12;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    private String appId;
}

