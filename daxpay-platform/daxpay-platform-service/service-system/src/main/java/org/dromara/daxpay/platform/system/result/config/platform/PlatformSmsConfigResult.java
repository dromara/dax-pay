package org.dromara.daxpay.platform.system.result.config.platform;

import org.dromara.daxpay.platform.common.json.sensitive.SensitiveInfo;
import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台短信配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台短信配置")
public class PlatformSmsConfigResult extends BaseResult {

    /// 配置名称
    @Schema(description = "配置名称")
    private String configName;

    /// 是否为默认
    @Schema(description = "是否为默认")
    private boolean enable;

    /// 短信服务商
    @Schema(description = "短信服务商")
    private String provider;

    /// 短信模板ID
    @Schema(description = "短信模板ID")
    private String templateId;

    /// 短信签名
    @Schema(description = "短信签名")
    private String signature;

    /// AccessKey
    @SensitiveInfo
    @Schema(description = "AccessKey")
    private String accessKey;

    /// SecretKey
    @SensitiveInfo
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
