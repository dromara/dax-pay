package org.dromara.daxpay.service.merchant.param.info;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户创建参数
 * @author xxm
 * @since 2025/6/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户参数")
public class MerchantCreateParam {

    /** 商户名称 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "商户名称")
    private String mchName;

    /** 是否创建默认应用 */
    @Schema(description = "是否创建默认应用")
    private Boolean createDefaultApp;

    @NotBlank(message = "用户名称不可为空")
    @Schema(description = "用户名称")
    private String name;

    @NotBlank(message = "登录账号不可为空")
    @Schema(description = "登录账号")
    private String account;

    @NotBlank(message = "密码不可为空")
    @Schema(description = "密码")
    private String password;

    public Boolean getCreateDefaultApp() {
        return Boolean.TRUE.equals(createDefaultApp);
    }
}
