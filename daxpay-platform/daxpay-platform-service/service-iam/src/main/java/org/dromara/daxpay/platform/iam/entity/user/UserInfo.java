package org.dromara.daxpay.platform.iam.entity.user;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.iam.code.UserStatusEnum;
import org.dromara.daxpay.platform.iam.convert.user.UserConvert;
import org.dromara.daxpay.platform.iam.param.user.UserInfoParam;
import org.dromara.daxpay.platform.iam.result.user.UserInfoResult;
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


