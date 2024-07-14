package cn.bootx.platform.iam.handler;

import cn.bootx.platform.core.annotation.InternalPath;
import cn.bootx.platform.core.exception.BizWarnException;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

/**
 * 内部接口鉴权处理
 * @see InternalPath
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InternalRouterCheck implements RouterCheck {

    @Override
    public int sortNo() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean check(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            // controller上是否加了内部接口注解
            InternalPath internalPath = handlerMethod.getBeanType().getAnnotation(InternalPath.class);
            if (Objects.isNull(internalPath)) {
                // 方法上上是否加了内部接口注解
                internalPath = handlerMethod.getMethodAnnotation(InternalPath.class);
            }
            // internalPath 不为空
            if (Objects.nonNull(internalPath)) {
                //
                if (!SecurityUtil.getUser().isAdmin()){
                    throw new BizWarnException("内部接口不允许普通用户进行调用!");
                }
            }
        }
        return false;
    }

}
