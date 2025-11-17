package org.dromara.daxpay.channel.wechat.param.config;

import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信支付配置")
public class WechatPaySubConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 微信子商户号 */
    @NotBlank(message = "微信子商户号不可为空")
    @Schema(description = "微信子商户号")
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
    @NotNull(message = "是否启用不可为空")
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;

}
