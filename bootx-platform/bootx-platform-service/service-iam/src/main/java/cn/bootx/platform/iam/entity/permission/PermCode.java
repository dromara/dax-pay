package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermCodeConvert;
import cn.bootx.platform.iam.result.permission.PermCodeResult;

/**
 * 权限码
 * @author xxm
 * @since 2024/7/3
 */
public class PermCode extends MpBaseEntity implements ToResult<PermCodeResult> {

    /** 权限码 */
    private String code;

    @Override
    public PermCodeResult toResult() {
        return PermCodeConvert.CONVERT.convert(this);
    }
}
