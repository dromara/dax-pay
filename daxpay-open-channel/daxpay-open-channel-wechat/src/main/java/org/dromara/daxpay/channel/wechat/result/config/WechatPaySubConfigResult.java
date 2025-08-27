package org.dromara.daxpay.channel.wechat.result.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信支付配置")
public class WechatPaySubConfigResult extends MchResult {

    /** 服务商户号 */
    @Schema(description = "服务商户号")
    private String spMchId;

    /** 服务商ID */
    @Schema(description = "服务商ID")
    private String spAppId;

    /** 特约商户号/二级商户号 */
    @Schema(description = "特约商户号/二级商户号")
    private String subMchId;

    /**
     * 授权类型
     * @see WechatAuthTypeEnum
     */
    @Schema(description = "授权类型")
    private String authType;

    /** 特约商户/二级商户应用ID */
    @Schema(description = "特约商户/二级商户应用ID")
    private String subAppId;

    /** 微信特约商户/二级商户密钥 */
    @Schema(description = "微信特约商户/二级商户密钥")
    private String wxAppSecret;

    /** 微信特约商户/二级商户授权认证地址 */
    @Schema(description = "微信特约商户/二级商户授权认证地址")
    private String wxAuthUrl;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

}
