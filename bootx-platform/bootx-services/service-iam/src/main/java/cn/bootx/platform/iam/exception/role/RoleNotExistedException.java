package cn.bootx.platform.iam.exception.role;

import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

import static cn.bootx.platform.iam.code.IamErrorCode.ROLE_NOT_EXISTED;

/**
 * 角色不存在
 *
 * @author xxm
 * @since 2020/5/7 18:04
 */
public class RoleNotExistedException extends BizException implements Serializable {

    private static final long serialVersionUID = -6651799569179960110L;

    public RoleNotExistedException() {
        super(ROLE_NOT_EXISTED, "角色不存在.");
    }

}
