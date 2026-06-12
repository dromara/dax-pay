package org.dromara.daxpay.platform.iam.exception.role;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.ROLE_ALREADY_USED;


public class RoleAlreadyUsedException extends BizException {

    public RoleAlreadyUsedException() {
        super(ROLE_ALREADY_USED, "该角色下分配了用户，您无法将其删除.");
        initMessageKey("error.iam.role.roleAlreadyUsed");
    }


    public RoleAlreadyUsedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
