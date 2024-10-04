package cn.bootx.platform.starter.auth.handler;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.starter.auth.exception.RouterCheckException;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.router.SaRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * 鉴权路由配置类
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaRouteHandler implements InitializingBean {

    private final List<RouterCheck> routerChecks;

    @Override
    public void afterPropertiesSet() {
        // 排序
        routerChecks.sort(Comparator.comparing(RouterCheck::sortNo));
    }

    /**
     * 路由检查处理方法
     */
    public SaFunction check(Object handler) {
        return () -> {
            boolean check = routerChecks.stream().anyMatch(routerCheck -> routerCheck.check(handler));
            if (check) {
                SaRouter.stop();
            }
            else {
                log.warn("{} 没有对应的权限", WebServletUtil.getPath());
                throw new RouterCheckException();
            }
        };
    }

}
