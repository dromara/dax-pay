package org.dromara.daxpay.platform.iam.entity.permission;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.iam.convert.permission.PermCodeConvert;
import org.dromara.daxpay.platform.iam.param.permission.resource.PermCodeParam;
import org.dromara.daxpay.platform.iam.result.permission.resource.PermCodeResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 权限码数据
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_code")
public class PermCodeData extends MpBaseEntity implements ToResult<PermCodeResult> {

    /// 权限码
    private String code;

    /// 中文名称
    private String nameCn;

    /// 英文名称
    private String nameEn;

    /// 菜单编码
    private String menuCode;

    /// 是否系统内置
    private boolean internal;

    /// 备注
    private String remark;

    public static PermCodeData init(PermCodeParam param) {
        return PermCodeConvert.CONVERT.convert(param);
    }

    @Override
    public PermCodeResult toResult() {
        return PermCodeConvert.CONVERT.convert(this);
    }
}
