package cn.bootx.platform.starter.auth.service;

import cn.bootx.platform.starter.auth.entity.UserStatus;

/**
 * 用户状态服务
 * @author xxm
 * @since 2023/11/25
 */
public interface UserStatusService {

    /**
     * 获取用户状态
     */
    UserStatus getUserStatus();
}
