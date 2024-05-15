package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.entity.AuthLoginType;

/**
 * 获取认证登录方式对象服务
 *
 * @author xxm
 * @since 2021/8/25
 */
public interface GetAuthLoginTypeService {

    /**
     * @param loginType 登录方式编码
     */
    AuthLoginType getAuthLoginType(String loginType);

}
