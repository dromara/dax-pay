package org.dromara.daxpay.platform.iam.entity.user;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.iam.convert.user.UserExpandConvert;
import org.dromara.daxpay.platform.iam.result.user.UserExpandInfoResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/// # 用户扩展信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_expand_info")
public class UserExpandInfo extends MpBaseEntity implements ToResult<UserExpandInfoResult> {

    /// 性别
    private String sex;

    /// 头像图片ID
    private String avatar;

    /// 生日
    private LocalDate birthday;

    /// 上次登录时间
    private LocalDateTime lastLoginTime;

    /// 注册时间
    private LocalDateTime registerTime;

    /// 本次登录时间
    private LocalDateTime currentLoginTime;

    /// 最后登录IP
    private String lastLoginIp;

    /// 登录次数
    private Integer loginCount;

    /// 注册来源
    private String registerSource;

    /// 注册渠道
    private String registerChannel;

    @Override
    public UserExpandInfoResult toResult() {
        return UserExpandConvert.CONVERT.convert(this);
    }

}
