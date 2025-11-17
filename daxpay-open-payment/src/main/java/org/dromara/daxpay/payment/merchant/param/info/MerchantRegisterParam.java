package org.dromara.daxpay.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户注册参数
 * @author xxm
 * @since 2025/6/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户注册参数")
public class MerchantRegisterParam {

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "服务商号")
    private String isvNo;

    @Schema(description = "用户绑定手机号")
    private String phone;

    @NotBlank(message = "登录账号不可为空")
    @Schema(description = "登录账号")
    private String account;

    @NotBlank(message = "密码不可为空")
    @Schema(description = "密码")
    private String password;
}
