package cn.daxpay.open.platform.iam.entity.role;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.iam.convert.role.RoleConvert;
import cn.daxpay.open.platform.iam.param.role.RoleParam;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 角色
///
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("iam_role")
@Accessors(chain = true)
public class Role extends MpBaseEntity implements ToResult<RoleResult> {

    /// 编码
    private String code;

    /// 中文名称
    private String nameCn;

    /// 英文名称
    private String nameEn;

    /// 终端编码: ADMIN/ISV/AGENT/MCH
    private String clientCode;

    /// 是否系统内置 不能修改
    private boolean internal;

    /// 备注
    private String remark;

    public static Role init(RoleParam in) {
        return RoleConvert.CONVERT.convert(in);
    }

    @Override
    public RoleResult toResult() {
        return RoleConvert.CONVERT.convert(this);
    }

}
