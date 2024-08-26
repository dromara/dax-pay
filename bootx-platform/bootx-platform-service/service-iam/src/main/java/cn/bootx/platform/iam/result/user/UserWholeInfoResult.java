package cn.bootx.platform.iam.result.user;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import cn.bootx.platform.core.result.BaseResult;
import cn.bootx.platform.iam.code.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户完整信息
 * @author xxm
 * @since 2024/8/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户完整信息")
public class UserWholeInfoResult extends BaseResult {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否管理员")
    private boolean administrator;

    /**
     * 账号状态
     * @see UserStatusEnum
     */
    @Schema(description = "账号状态")
    private String status;

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
