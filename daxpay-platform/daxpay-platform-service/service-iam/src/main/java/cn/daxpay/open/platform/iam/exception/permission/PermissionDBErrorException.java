package cn.daxpay.open.platform.iam.exception.permission;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.iam.code.IamErrorCode;

public class PermissionDBErrorException extends BizException {

    public PermissionDBErrorException() {
        super(IamErrorCode.PERMISSION_DB_ERROR, "用户没有权限.");
        initMessageKey("error.iam.role.permissionDbError");
    }

    public PermissionDBErrorException(int code, String messageKey, Object... args) {
        super(code, messageKey, args);
    }
}
