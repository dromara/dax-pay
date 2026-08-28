package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(title = "用户基础信息")
public class UserBaseInfoParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

    /// email 不在本参数中受理: 邮箱是找回密码的安全凭证,
    /// 变更仅允许走 /user/auth/email 绑定验证流程

    @Schema(description = "手机号")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "{validation.field.phone.format}")
    private String phone;

}
