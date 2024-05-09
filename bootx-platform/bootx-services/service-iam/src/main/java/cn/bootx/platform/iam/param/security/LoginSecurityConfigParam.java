package cn.bootx.platform.iam.param.security;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023-09-19
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Schema(title = "登录安全策略")
@Accessors(chain = true)
public class LoginSecurityConfigParam {

    @Schema(description= "主键")
    private Long id;

    @Schema(description = "关联终端ID")
    private Long clientId;
    @Schema(description = "最大密码错误数")
    private Integer maxPwdErrorCount;
    @Schema(description = "密码错误锁定时间(秒)")
    private Integer errorLockTime;
    @Schema(description = "")
    private Boolean requireChangePwd;
    @Schema(description = "修改密码是否需要重新登录")
    private Boolean requireLoginChangePwd;
    @Schema(description = "默认启用验证码")
    private Boolean captchaEnable;
    @Schema(description = "出现验证码的错误次数")
    private Integer maxCaptchaErrorCount;
    @Schema(description = "同端是否允许同时登录")
    private Boolean allowMultiLogin;
    @Schema(description = "多终端是否允许同时登录")
    private Boolean allowMultiTerminalLogin;

}
