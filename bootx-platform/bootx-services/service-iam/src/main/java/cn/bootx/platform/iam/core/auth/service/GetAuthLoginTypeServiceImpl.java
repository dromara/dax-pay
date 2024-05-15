package cn.bootx.platform.iam.core.auth.service;

import cn.bootx.platform.iam.core.client.dao.LoginTypeManager;
import cn.bootx.platform.iam.core.client.entity.LonginType;
import cn.bootx.platform.starter.auth.authentication.GetAuthLoginTypeService;
import cn.bootx.platform.starter.auth.entity.AuthLoginType;
import cn.bootx.platform.starter.auth.exception.ClientNotFoundException;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 获取认证终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuthLoginTypeServiceImpl implements GetAuthLoginTypeService {

    private final LoginTypeManager loginTypeManager;

    /**
     * 获取认证终端信息
     */
    @Override
    public AuthLoginType getAuthLoginType(String loginType) {
        LonginType longinType = loginTypeManager.findByCode(loginType).orElseThrow(ClientNotFoundException::new);
        AuthLoginType authLoginType = new AuthLoginType();
        BeanUtil.copyProperties(longinType, authLoginType);
        return authLoginType;
    }

}
