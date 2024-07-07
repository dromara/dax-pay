package cn.bootx.platform.starter.auth.cache;

import cn.bootx.platform.core.entity.UserDetail;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 会话缓存线程存储
 *
 * @author xxm
 * @since 2022/1/8
 */
@UtilityClass
public class SessionCacheLocal {

    private final ThreadLocal<UserDetail> USER_INFO_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置用户信息
     */
    public void putUserInfo(UserDetail userDetail) {
        USER_INFO_LOCAL.set(userDetail);
    }

    /**
     * 获取用户信息
     */
    public UserDetail getUserInfo() {
        return USER_INFO_LOCAL.get();
    }


    /**
     * 清除
     */
    public static void clear() {
        USER_INFO_LOCAL.remove();
    }

}
