package cn.bootx.platform.iam.core.security.user.service;

import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户安全配置
 * @author xxm
 * @since 2023/9/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSecurityConfigService {

    /**
     * 获取用户的安全状态, 登陆后主要用来确定用户是否需要进行哪些的校验
     */
    public void getUserCheckSecurity(){
        Long userId = SecurityUtil.getUserId();
    }
}
