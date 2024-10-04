package cn.bootx.platform.iam.entity.role;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.role.RoleConvert;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.iam.result.role.RoleResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色
 *
 * @author xxm
 * @since 2020/5/1 17:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("iam_role")
@Accessors(chain = true)
public class Role extends MpBaseEntity implements ToResult<RoleResult> {

    /** 父级Id */
    private Long pid;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否系统内置 不能修改 */
    private boolean internal;

    /** 备注 */
    private String remark;

    public static Role init(RoleParam in) {
        return RoleConvert.CONVERT.convert(in);
    }

    @Override
    public RoleResult toResult() {
        return RoleConvert.CONVERT.convert(this);
    }

}
