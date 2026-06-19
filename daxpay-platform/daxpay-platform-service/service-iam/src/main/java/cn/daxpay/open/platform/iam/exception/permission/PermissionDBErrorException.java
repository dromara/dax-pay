package cn.daxpay.open.platform.iam.exception.permission;

import cn.daxpay.open.platform.core.exception.BizException;

import static cn.daxpay.open.platform.iam.code.IamErrorCode.PERMISSION_DB_ERROR;


public class PermissionDBErrorException extends BizException {

    public PermissionDBErrorException() {
        super(PERMISSION_DB_ERROR, "用户没有权限.");
        initMessageKey("error.iam.role.permissionDbError");
    }


    public PermissionDBErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
