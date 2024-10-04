package cn.bootx.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户扩展信息 whole
 * @author xxm
 * @since 2022/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户扩展信息")
public class UserExpandInfoResult {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "上次登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "本次登录时间")
    private LocalDateTime currentLoginTime;

    @Schema(description = "是否初始密码")
    private boolean initialPassword;

    @Schema(description = "密码是否过期")
    private boolean expirePassword;

    @Schema(description = "上次修改密码时间")
    private LocalDateTime lastChangePasswordTime;

}
