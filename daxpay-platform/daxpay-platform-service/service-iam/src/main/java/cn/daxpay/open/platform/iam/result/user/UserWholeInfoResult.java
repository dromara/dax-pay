package cn.daxpay.open.platform.iam.result.user;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/// # 用户完整信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户完整信息")
public class UserWholeInfoResult extends BaseResult {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    private String password;

    @Schema(description = "邮箱")
    @SensitiveInfo(SensitiveInfo.SensitiveType.EMAIL)
    private String email;

    @Schema(description = "是否管理员")
    private boolean administrator;

    /// 账号状态
    /// @see UserStatusEnum
    @Schema(description = "账号状态")
    private String status;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "上次登录时间 (UTC)")
    private OffsetDateTime lastLoginTime;

    @Schema(description = "本次登录时间 (UTC)")
    private OffsetDateTime currentLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "登录次数")
    private Integer loginCount;

    @Schema(description = "注册来源")
    private String registerSource;

    @Schema(description = "注册渠道")
    private String registerChannel;
}

