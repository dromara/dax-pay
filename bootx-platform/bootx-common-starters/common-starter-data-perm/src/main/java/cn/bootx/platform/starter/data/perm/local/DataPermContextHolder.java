package cn.bootx.platform.starter.data.perm.local;

import cn.bootx.platform.common.core.annotation.NestedPermission;
import cn.bootx.platform.common.core.annotation.Permission;
import cn.bootx.platform.common.core.entity.UserDetail;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

/**
 * 忽略鉴权数据上下文
 *
 * @author xxm
 * @since 2021/12/22
 */
public class DataPermContextHolder {

    private static final ThreadLocal<Permission> PERMISSION_LOCAL = new TransmittableThreadLocal<>();

    private static final ThreadLocal<NestedPermission> NESTED_PERMISSION_LOCAL = new TransmittableThreadLocal<>();

    private static final ThreadLocal<UserDetail> USER_DETAIL_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置 数据权限控制注解
     */
    public static void putPermission(Permission permission) {
        PERMISSION_LOCAL.set(permission);
    }

    /**
     * 获取 数据权限控制注解
     */
    public static Permission getPermission() {
        return PERMISSION_LOCAL.get();
    }

    /**
     * 设置 数据权限控制注解
     */
    public static void putNestedPermission(NestedPermission nestedPermission) {
        NESTED_PERMISSION_LOCAL.set(nestedPermission);
    }

    /**
     * 获取 数据权限控制注解
     */
    public static NestedPermission getNestedPermission() {
        return NESTED_PERMISSION_LOCAL.get();
    }

    /**
     * 设置 用户缓存
     */
    public static void putUserDetail(UserDetail dataPerm) {
        USER_DETAIL_LOCAL.set(dataPerm);
    }

    /**
     * 获取 用户缓存
     */
    public static Optional<UserDetail> getUserDetail() {
        return Optional.ofNullable(USER_DETAIL_LOCAL.get());
    }

    /**
     * 清除线程变量(数据权限控制和用户信息)
     */
    public static void clearUserAndPermission() {
        USER_DETAIL_LOCAL.remove();
        PERMISSION_LOCAL.remove();
    }

    /**
     * 清除线程变量(嵌套权限注解)
     */
    public static void clearNestedPermission() {
        NESTED_PERMISSION_LOCAL.remove();
    }

}
