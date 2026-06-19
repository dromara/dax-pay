package cn.daxpay.open.platform.iam.entity.user;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.iam.convert.user.UserPasswordSecurityConvert;
import cn.daxpay.open.platform.iam.result.user.UserPasswordSecurityResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 用户密码安全信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_password_security")
public class UserPasswordSecurity extends MpBaseEntity implements ToResult<UserPasswordSecurityResult> {

    /// 密码错误次数
    private Integer passwordErrorCount;

    /// 锁定结束时间 (UTC)
    private OffsetDateTime lockTime;

    /// 密码过期时间 (UTC)
    private OffsetDateTime passwordExpireTime;

    /// 上次修改密码时间 (UTC)
    private OffsetDateTime lastChangePasswordTime;

    /// 是否初始密码
    private Boolean initialPassword;

    /// 上次登录失败时间 (UTC)
    private OffsetDateTime lastFailureTime;

    @Override
    public UserPasswordSecurityResult toResult() {
        return UserPasswordSecurityConvert.CONVERT.convert(this);
    }
}
