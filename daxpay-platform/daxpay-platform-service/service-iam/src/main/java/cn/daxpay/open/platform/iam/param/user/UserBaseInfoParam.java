package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
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

    /// email 与手机号均不在本参数中受理:
    /// email 是找回密码的安全凭证, 变更仅允许走 /user/auth/email 绑定验证流程;
    /// 手机号功能已冻结(无短信验证体系, 待接入后以验证码方式启用)

}
