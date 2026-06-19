package cn.daxpay.open.platform.iam.exception.role;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.iam.code.IamErrorCode.ROLE_ALREADY_EXISTED;


public class RoleAlreadyExistedException extends BizException {

    public RoleAlreadyExistedException() {
        super(ROLE_ALREADY_EXISTED, "角色已经存在.");
        initMessageKey("error.iam.role.roleAlreadyExisted");
    }


    public RoleAlreadyExistedException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
