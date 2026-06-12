package org.dromara.daxpay.platform.iam.entity.user;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.iam.convert.user.UserPasswordSecurityConvert;
import org.dromara.daxpay.platform.iam.result.user.UserPasswordSecurityResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// # 用户密码安全信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_password_security")
public class UserPasswordSecurity extends MpBaseEntity implements ToResult<UserPasswordSecurityResult> {

    /// 密码错误次数
    private Integer passwordErrorCount;

    /// 锁定结束时间
    private LocalDateTime lockTime;

    /// 密码过期时间
    private LocalDateTime passwordExpireTime;

    /// 上次修改密码时间
    private LocalDateTime lastChangePasswordTime;

    /// 是否初始密码
    private Boolean initialPassword;

    /// 上次登录失败时间
    private LocalDateTime lastFailureTime;

    @Override
    public UserPasswordSecurityResult toResult() {
        return UserPasswordSecurityConvert.CONVERT.convert(this);
    }
}
