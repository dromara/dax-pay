package cn.daxpay.open.platform.iam.entity.user;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.convert.user.UserConvert;
import cn.daxpay.open.platform.iam.param.user.UserInfoParam;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户核心信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_info")
public class UserInfo extends MpBaseEntity implements ToResult<UserInfoResult> {

    /// 名称
    private String name;

    /// 终端编码
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clientCode;

    /// 账号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String account;

    /// 密码
    private String password;

    /// 手机号
    private String phone;

    /// 邮箱
    private String email;

    /// 邮箱是否已验证(走邮箱验证码流程后置true), 未验证邮箱不可用于找回密码
    private boolean emailVerified;

    /// 是否管理员, 管理员用户不在列表中显示
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private boolean administrator;

    /// 账号状态
    /// @see UserStatusEnum
    private String status;

    @Override
    public UserInfoResult toResult() {
        return UserConvert.CONVERT.convert(this);
    }

    public static UserInfo init(UserInfoParam param) {
        return UserConvert.CONVERT.convert(param);
    }

}

