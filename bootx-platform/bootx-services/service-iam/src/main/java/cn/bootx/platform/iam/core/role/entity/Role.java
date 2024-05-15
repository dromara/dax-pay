package cn.bootx.platform.iam.core.role.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.iam.core.role.convert.RoleConvert;
import cn.bootx.platform.iam.param.role.RoleParam;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.dto.role.RoleDto;
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
public class Role extends MpBaseEntity implements EntityBaseFunction<RoleDto> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否系统内置 不能修改 */
    private boolean internal;

    /** 父级Id */
    private Long pid;

    /** 描述 */
    private String remark;

    public static Role init(RoleParam in) {
        return RoleConvert.CONVERT.convert(in);
    }

    @Override
    public RoleDto toDto() {
        return RoleConvert.CONVERT.convert(this);
    }

}
