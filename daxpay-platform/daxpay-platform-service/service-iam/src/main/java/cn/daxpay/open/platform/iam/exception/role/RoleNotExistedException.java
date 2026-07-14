package cn.daxpay.open.platform.iam.exception.role;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.IamErrorCode;

/// # 角色不存在
///
public class RoleNotExistedException extends BizException {

    public RoleNotExistedException() {
        super(IamErrorCode.ROLE_NOT_EXISTED, "角色不存在.");
        initMessageKey("error.iam.role.roleNotExisted");
    }

    public RoleNotExistedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
