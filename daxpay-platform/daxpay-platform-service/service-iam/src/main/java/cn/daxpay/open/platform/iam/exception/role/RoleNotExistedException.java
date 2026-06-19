package cn.daxpay.open.platform.iam.exception.role;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.iam.code.IamErrorCode.ROLE_NOT_EXISTED;

/// # 角色不存在
///
public class RoleNotExistedException extends BizException {

    public RoleNotExistedException() {
        super(ROLE_NOT_EXISTED, "角色不存在.");
        initMessageKey("error.iam.role.roleNotExisted");
    }


    public RoleNotExistedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
