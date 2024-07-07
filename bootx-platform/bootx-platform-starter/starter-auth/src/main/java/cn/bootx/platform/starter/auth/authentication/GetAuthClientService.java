package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.entity.AuthClient;

/**
 * 获取认证终端
 *
 * @author xxm
 * @since 2022/6/27
 */
public interface GetAuthClientService {

    /**
     * 认证应用
     */
    AuthClient getAuthApplication(String authApplicationCode);

}
