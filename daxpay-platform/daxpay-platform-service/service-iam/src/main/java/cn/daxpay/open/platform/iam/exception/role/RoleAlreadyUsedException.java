package cn.daxpay.open.platform.iam.exception.role;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.IamErrorCode;

public class RoleAlreadyUsedException extends BizException {

    public RoleAlreadyUsedException() {
        super(IamErrorCode.ROLE_ALREADY_USED, "该角色下分配了用户，您无法将其删除.");
        initMessageKey("error.iam.role.roleAlreadyUsed");
    }

    public RoleAlreadyUsedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
