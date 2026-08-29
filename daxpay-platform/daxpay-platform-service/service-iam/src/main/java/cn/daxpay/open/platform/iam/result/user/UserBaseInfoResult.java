package cn.daxpay.open.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(title = "用户基础信息")
public class UserBaseInfoResult {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "邮箱")
    private String email;

}
