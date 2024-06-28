package cn.daxpay.multi.channel.alipay.param.config;

import cn.daxpay.multi.channel.alipay.code.AliPayCode;
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
    @Schema(title = "主键")
    private Long id;

    /** 商户号 */
    @Schema(title = "商户号")
    private String mchNo;

    /** 商户AppId */
    @Schema(title = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;

    /** 支付宝商户appId */
    @Schema(title = "支付宝商户appId")
    private String outAppId;

    /** 是否启用 */
    @Schema(title = "是否启用")
    @NotBlank(message = "是否启用不可为空")
    private Boolean enable;

    /** 支付限额 */
    @Schema(title = "支付限额")
    @NotBlank(message = "支付限额不可为空")
    private BigDecimal limitAmount;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 调用顺序 支付宝网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    @Schema(title = "服务器异步通知页面路径")
    @NotBlank(message = "服务器异步通知页面路径不可为空")
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 支付宝网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    @Schema(title = "服务器同步通知页面路径")
    @NotBlank(message = "服务器同步通知页面路径不可为空")
    private String returnUrl;

    /** 支付网关地址 */
    @Schema(title = "支付网关地址")
    @NotBlank(message = "支付网关地址不可为空")
    private String serverUrl;

    /** 授权回调地址 */
    @Schema(title = "授权回调地址")
    private String redirectUrl;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    @Schema(title = "认证类型")
    @NotBlank(message = "认证类型不可为空")
    private String authType;

    /** 签名类型 RSA2 */
    @Schema(title = "签名类型")
    @NotBlank(message = "签名类型不可为空")
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @Schema(title = "支付宝商家唯一识别码")
    private String alipayUserId;

    /** 支付宝公钥 */
    @Schema(title = "支付宝公钥")
    public String alipayPublicKey;

    /** 私钥 */
    @Schema(title = "私钥")
    private String privateKey;

    /** 应用公钥证书 */
    @Schema(title = "应用公钥证书")
    private String appCert;

    /** 支付宝公钥证书 */
    @Schema(title = "支付宝公钥证书")
    private String alipayCert;

    /** 支付宝CA根证书 */
    @Schema(title = "支付宝CA根证书")
    private String alipayRootCert;

    /** 是否沙箱环境 */
    @Schema(title = "是否沙箱环境")
    private boolean sandbox;
}
