package cn.bootx.platform.starter.auth.cache;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.starter.auth.entity.UserStatus;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 会话缓存线程存储
 *
 * @author xxm
 * @since 2022/1/8
 */
public final class SessionCacheLocal {

    private static final ThreadLocal<UserDetail> USER_INFO_LOCAL = new TransmittableThreadLocal<>();
    private static final ThreadLocal<UserStatus> USER_STATUS_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置用户信息
     */
    public static void putUserInfo(UserDetail userDetail) {
        USER_INFO_LOCAL.set(userDetail);
    }

    /**
     * 获取用户信息
     */
    public static UserDetail getUserInfo() {
        return USER_INFO_LOCAL.get();
    }

    /**
     * 设置用户状态
     */
    public static void putUserStatus(UserStatus userStatus) {
        USER_STATUS_LOCAL.set(userStatus);
    }

    /**
     * 获取用户状态上下文
     */
    public static UserStatus getUserStatusContext() {
        return USER_STATUS_LOCAL.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        USER_INFO_LOCAL.remove();
        USER_STATUS_LOCAL.remove();
    }

}
