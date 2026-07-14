package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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

    @Schema(description = "邮箱")
    @Email(message = "{validation.field.email.format}")
    private String email;

    @Schema(description = "手机号")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "{validation.field.phone.format}")
    private String phone;

}
