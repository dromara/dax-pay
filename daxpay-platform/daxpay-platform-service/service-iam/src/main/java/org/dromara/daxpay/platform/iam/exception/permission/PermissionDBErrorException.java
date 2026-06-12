package org.dromara.daxpay.platform.iam.exception.permission;

import org.dromara.daxpay.platform.core.exception.BizException;

import static org.dromara.daxpay.platform.iam.code.IamErrorCode.PERMISSION_DB_ERROR;


public class PermissionDBErrorException extends BizException {

    public PermissionDBErrorException() {
        super(PERMISSION_DB_ERROR, "用户没有权限.");
        initMessageKey("error.iam.role.permissionDbError");
    }


    public PermissionDBErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
