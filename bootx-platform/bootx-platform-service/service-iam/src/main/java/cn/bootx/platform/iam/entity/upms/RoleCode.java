package cn.bootx.platform.iam.entity.upms;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色权限码关联关系
 * @author xxm
 * @since 2024/7/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleCode extends MpIdEntity {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 终端类型
     */
    private String clientCode;

    /**
     * 权限码
     */
    private String code;
}
