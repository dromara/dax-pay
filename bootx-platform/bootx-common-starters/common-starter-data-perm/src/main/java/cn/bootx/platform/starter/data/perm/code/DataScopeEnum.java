package cn.bootx.platform.starter.data.perm.code;

import cn.bootx.platform.common.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据范围权限类型
 *
 * @author xxm
 * @since 2021/12/22
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /** 自己的数据 */
    SELF("self"),
    /** 指定用户级别 */
    USER_SCOPE("user"),
    /** 指定部门级别 */
    DEPT_SCOPE("dept"),
    /** 指定部门和下级部门 */
    DEPT_SCOPE_AND_SUB("dept_sub"),
    /** 指定部门与用户级别 */
    DEPT_AND_USER_SCOPE("dept_and_user"),
    /** 全部数据 */
    ALL_SCOPE("all"),
    /** 所在部门 */
    SELF_DEPT("self_dept"),
    /** 所在及下级部门 */
    SELF_DEPT_AND_SUB("self_dept_sub");

    private final String code;

    /**
     * 根据数字编号获取
     */
    public static DataScopeEnum findByCode(String code) {
        return Arrays.stream(DataScopeEnum.values())
            .filter(e -> Objects.equals(e.getCode(), code))
            .findFirst()
            .orElseThrow(() -> new BizException("不支持的数据权限类型"));
    }

}
