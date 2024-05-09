package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.websocket.func.WsUserAuthService;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * websocket 用户认证接口实现
 *
 * @author xxm
 * @since 2022/6/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WsUserAuthServiceImpl implements WsUserAuthService {

    @Override
    public Long getUserIdByToken(String token) {
        return Optional.ofNullable(StpUtil.getLoginIdByToken(token))
            .map(id -> (String) id)
            .map(Long::valueOf)
            .orElse(null);
    }

}
