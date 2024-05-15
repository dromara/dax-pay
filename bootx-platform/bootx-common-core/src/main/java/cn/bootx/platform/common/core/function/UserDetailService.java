package cn.bootx.platform.common.core.function;

import cn.bootx.platform.common.core.entity.UserDetail;

import java.util.Optional;

/**
 * 获取用户
 *
 * @author xxm
 * @since 2022/8/28
 */
public interface UserDetailService {

    /**
     * 获取用户信息
     */
    Optional<UserDetail> findByUserId(Long userId);

}
