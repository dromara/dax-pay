package cn.bootx.platform.iam.exception.role;

import cn.bootx.platform.core.exception.BizException;

import static cn.bootx.platform.iam.code.IamErrorCode.ROLE_ALREADY_USED;

/**
 * @author xxm
 * @since 2020/4/29 14:42
 */
public class RoleAlreadyUsedException extends BizException {

    public RoleAlreadyUsedException() {
        super(ROLE_ALREADY_USED, "该角色下分配了用户，您无法将其删除.");
    }

}
