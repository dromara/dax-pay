package org.dromara.daxpay.payment.merchant.param.info;

import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户注册参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户注册参数")
public class MerchantRegisterParam {

    /// 商户名称
    @NotBlank(message = "{validation.field.mchName.notBlank}")
    @Schema(description = "商户名称")
    private String mchName;

    /// 商户简称
    @NotBlank(message = "{validation.field.mchShortName.notBlank}")
    @Schema(description = "商户简称")
    private String mchShortName;

    /// 主体类型
    /// @see SubjectTypeEnum
    @NotBlank(message = "{validation.field.subjectType.notBlank}")
    @Schema(description = "主体类型")
    private String subjectType;

    @Schema(description = "用户绑定手机号")
    private String phone;

    @Schema(description = "验证码")
    private String smsCaptcha;

    @NotBlank(message = "{validation.field.account.notBlank}")
    @Schema(description = "登录账号")
    private String account;

    @NotBlank(message = "{validation.field.password.notBlank}")
    @Schema(description = "密码")
    private String password;
}

