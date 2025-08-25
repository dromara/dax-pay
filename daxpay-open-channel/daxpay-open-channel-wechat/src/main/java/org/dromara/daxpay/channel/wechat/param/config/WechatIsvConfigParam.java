package org.dromara.daxpay.channel.wechat.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;

/**
 * 微信服务商配置参数
 * @author xxm
 * @since 2024/11/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商配置参数")
public class WechatIsvConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 微信商户Id */
    @NotBlank(message = "微信商户Id不可为空")
    @Schema(description = "微信商户Id")
    private String wxMchId;

    /** 微信应用appId */
    @NotBlank(message = "微信应用appId不可为空")
    @Schema(description = "微信应用appId")
    private String wxAppId;

    /** 是否启用 */
    @NotNull(message = "是否启用不可为空")
    @Schema(description = "是否启用")
    private Boolean enable;

    /**
     * 接口版本, 使用v2还是v3接口
     * @see WechatPayCode#API_V2
     */
    @NotBlank(message = "接口版本不可为空")
    @Schema(description = "接口版本")
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @Schema(description = "APIv2 密钥")
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @Schema(description = "APIv3 密钥")
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @Schema(description = "APPID对应的接口密码，用于获取微信公众号jsapi支付时使用")
    private String appSecret;

    /** 支付公钥(pub_key.pem) */
    @Schema(description = "支付公钥(pub_key.pem)")
    private String publicKey;

    /** 支付公钥ID */
    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    /** apiclient_key.pem证书base64编码 */
    @Schema(description = "私钥证书base64编码")
    private String privateKey;

    /** apiclient_cert.pem证书base64编码 */
    @Schema(description = "私钥Key的base64编码")
    private String privateCert;

    /** 证书序列号 */
    @Schema(description = "证书序列号")
    private String certSerialNo;

    /** apiclient_cert.p12证书Base64 */
    @Schema(description = "API证书中p12证书")
    private String p12;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

}
