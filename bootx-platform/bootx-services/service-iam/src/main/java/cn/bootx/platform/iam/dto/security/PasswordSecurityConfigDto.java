package cn.bootx.platform.iam.dto.security;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 密码安全策略
 * @author xxm
 * @since 2023-09-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "密码安全策略")
@Accessors(chain = true)
public class PasswordSecurityConfigDto extends BaseDto {

    @Schema(description = "最大密码错误数")
    private int maxPwdErrorCount;
    @Schema(description = "密码错误锁定时间(分钟)")
    private int errorLockTime;
    @Schema(description = "密码安全级别")
    private String securityLevel;
    @Schema(description = "强制修改初始密码")
    private boolean requireChangePwd;
    @Schema(description = "密码更新频率(天)")
    private int updateFrequency;
    @Schema(description = "到期提醒(天)")
    private int expireRemind;
    @Schema(description = "允许与登录名相同")
    private boolean sameAsLoginName;
    @Schema(description = "不能与近期多少次密码相同")
    private int recentPassword;

    /**
     * 默认配置
     */
    public static PasswordSecurityConfigDto defaultObject() {
        return new PasswordSecurityConfigDto()
                .setMaxPwdErrorCount(5)
                .setErrorLockTime(10)
                .setRequireChangePwd(true)
                .setUpdateFrequency(90)
                .setExpireRemind(15)
                .setSameAsLoginName(false)
                .setRecentPassword(5);
    }
}
