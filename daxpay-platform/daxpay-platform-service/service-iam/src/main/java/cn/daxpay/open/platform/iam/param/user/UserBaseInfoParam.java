package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;


@Data
@Accessors(chain = true)
@Schema(title = "用户基础信息")
public class UserBaseInfoParam {

    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

}
