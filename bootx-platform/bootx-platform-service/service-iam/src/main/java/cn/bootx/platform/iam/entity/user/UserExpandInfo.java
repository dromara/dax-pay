package cn.bootx.platform.iam.entity.user;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.user.UserExpandConvert;
import cn.bootx.platform.iam.result.user.UserExpandInfoResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户扩展信息
 *
 * @author xxm
 * @since 2022/1/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_expand_info")
public class UserExpandInfo extends MpBaseEntity implements ToResult<UserExpandInfoResult> {

    /** 性别 */
    private String sex;

    /** 头像图片ID */
    private String avatar;

    /** 生日 */
    private LocalDate birthday;

    /** 上次登录时间 */
    private LocalDateTime lastLoginTime;

    /** 注册时间 */
    private LocalDateTime registerTime;

    /** 本次登录时间 */
    private LocalDateTime currentLoginTime;

    /** 是否初始密码 */
    private boolean initialPassword;

    /** 密码是否过期 */
    private boolean expirePassword;

    /** 上次修改密码时间 */
    private LocalDateTime lastChangePasswordTime;

    @Override
    public UserExpandInfoResult toResult() {
        return UserExpandConvert.CONVERT.convert(this);
    }

}
