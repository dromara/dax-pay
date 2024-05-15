package cn.bootx.platform.starter.data.perm.scope;

import cn.bootx.platform.starter.data.perm.code.DataScopeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 数据权限范围参数
 *
 * @author xxm
 * @since 2021/12/22
 */
@Data
@Accessors(chain = true)
public class DataPermScope {

    /**
     * 范围类型 自身,部门,人员,部门和人员
     */
    private DataScopeEnum scopeType;

    /** 对应部门ID集合 */
    private Set<Long> deptScopeIds;

    /** 对应用户ID集合 */
    private Set<Long> UserScopeIds;

}
