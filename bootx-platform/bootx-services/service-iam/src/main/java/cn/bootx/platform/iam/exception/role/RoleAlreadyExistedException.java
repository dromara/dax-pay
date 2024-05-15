package cn.bootx.platform.iam.exception.role;

import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

import static cn.bootx.platform.iam.code.IamErrorCode.ROLE_ALREADY_EXISTED;

/**
 * @author xxm
 * @since 2020/4/29 14:37
 */
public class RoleAlreadyExistedException extends BizException implements Serializable {

    private static final long serialVersionUID = -9126473695763034719L;

    public RoleAlreadyExistedException() {
        super(ROLE_ALREADY_EXISTED, "角色已经存在.");
    }

}
