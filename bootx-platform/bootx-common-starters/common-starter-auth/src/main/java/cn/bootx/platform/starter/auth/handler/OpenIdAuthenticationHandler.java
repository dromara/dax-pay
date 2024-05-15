package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * OpenID登录处理器
 *
 * @author xxm
 * @since 2021/7/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenIdAuthenticationHandler {

    private final List<OpenIdAuthentication> openIdAuthentications;

    /**
     * 认证
     */
    public @NotNull AuthInfoResult authentication(LoginAuthContext context) {
        String clientCode = context.getAuthLoginType().getCode();
        return openIdAuthentications.stream()
            .filter(o -> o.adaptation(clientCode))
            .findFirst()
            .map(o -> o.authentication(context))
            .orElseThrow(() -> new LoginFailureException("未找到对应的终端认证器"));
    }

}
