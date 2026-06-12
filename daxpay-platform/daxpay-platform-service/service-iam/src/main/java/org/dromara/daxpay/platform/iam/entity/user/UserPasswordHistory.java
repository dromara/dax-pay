package org.dromara.daxpay.platform.iam.entity.user;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户密码历史
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_password_history")
public class UserPasswordHistory extends MpCreateEntity {

    /// 用户ID
    private Long userId;

    /// 历史密码
    private String password;
}
