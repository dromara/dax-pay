package cn.daxpay.open.platform.iam.param.user;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "用户信息参数")
public class UserInfoParam {

    @Schema(description = "主键")
    @NotNull(groups = ValidationGroup.edit.class, message = "{validation.field.id.notNull}")
    private Long id;

    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.name.notBlank}")
    @Size(min = 3, max = 15, message = "{validation.field.name.size}")
    @Schema(description = "名称")
    private String name;

    @Schema(description = "终端编码")
    private String clientCode;

    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.account.notBlank}")
    @Size(min = 6, max = 20, message = "{validation.field.account.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.field.account.pattern}")
    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "密码(RSA 加密), 可选; 不传时由系统生成随机初始密码并在响应中返回明文")
    private String password;

    /// 手机号定位为管理员维护的联系信息(无短信验证体系), 保留管理端可编辑
    @Schema(description = "手机号")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "{validation.field.phone.format}")
    private String phone;

    /// email 不在本参数中受理: 邮箱是找回密码的安全凭证,
    /// 变更仅允许用户本人走 /user/auth/email 绑定验证流程;
    /// 管理员仅有强制解绑能力(/user/admin/unbind-email), 不可指定新邮箱

}

