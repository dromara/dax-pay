package org.dromara.daxpay.platform.system.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台短信配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "平台短信配置参数")
public class PlatformSmsConfigParam {

    /// ID
    @Schema(description = "ID")
    private Long id;

    /// 配置名称
    @NotBlank(message = "{validation.field.configName.notBlank}")
    @Schema(description = "配置名称")
    private String configName;

    /// 短信服务商
    @NotBlank(message = "{validation.field.smsProvider.notBlank}")
    @Schema(description = "短信服务商")
    private String provider;

    /// 短信签名
    @Schema(description = "短信签名")
    private String signature;

    /// AccessKey
    @Schema(description = "AccessKey")
    private String accessKey;

    /// SecretKey
    @Schema(description = "SecretKey")
    private String secretKey;

    /// 商户注册模板
    @Schema(description = "商户注册模板")
    private String registerId;

    /// 忘记密码模板
    @Schema(description = "忘记密码模板")
    private String forgetId;

    /// 验证码模板
    @Schema(description = "验证码模板")
    private String captchaId;

    /// 通知模板
    @Schema(description = "通知模板")
    private String noticeId;

}
