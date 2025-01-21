package cn.bootx.platform.iam.exception.role;

import cn.bootx.platform.core.exception.BizException;

import static cn.bootx.platform.iam.code.IamErrorCode.ROLE_HAS_CHILD;

/**
 * 含有下级角色异常
 * @author xxm
 * @since 2023/12/5
 */
public class RoleHasChildrenException extends BizException {

    public RoleHasChildrenException() {
        super(ROLE_HAS_CHILD, "该角色下分配了用户，您无法将其删除.");
    }
}
