package cn.bootx.platform.iam.exception.permission;

import cn.bootx.platform.core.exception.BizException;

import static cn.bootx.platform.iam.code.IamErrorCode.PERMISSION_DB_ERROR;

/**
 * @author xxm
 * @since 2020/5/7 18:01
 */
public class PermissionDBErrorException extends BizException {

    public PermissionDBErrorException() {
        super(PERMISSION_DB_ERROR, "用户没有权限.");
    }

}
