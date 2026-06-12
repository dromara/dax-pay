package org.dromara.daxpay.platform.iam.exception.role;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.ROLE_NOT_EXISTED;

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
