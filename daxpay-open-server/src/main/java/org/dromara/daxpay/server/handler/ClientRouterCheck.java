package org.dromara.daxpay.server.handler;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import cn.bootx.platform.starter.auth.exception.RouterCheckException;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;

/**
 * 终端路径过滤
 * @author xxm
 * @since 2025/1/31
 */
@Component
@RequiredArgsConstructor
public class ClientRouterCheck implements RouterCheck {
    private final ClientCodeService clientCodeService;

    @Override
    public int sortNo() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean check(Object handler) {
        // 获取当前终端编码
        String clientCode = clientCodeService.getClientCode();
        // 判断当前编码是否包含在白名单中
        if (handler instanceof HandlerMethod handlerMethod) {
            // controller上是否加了跳过鉴权注解
            var clientCodeEnum = handlerMethod.getBeanType().getAnnotation(ClientCode.class);
            if (Objects.isNull(clientCodeEnum)) {
                // 方法上上是否加了跳过鉴权注解
                clientCodeEnum = handlerMethod.getMethodAnnotation(ClientCode.class);
            }
            else {
                // controller和方法上都加了跳过鉴权注解,以方法上为准
                var annotation = handlerMethod.getMethodAnnotation(ClientCode.class);
                if (Objects.nonNull(annotation)) {
                    clientCodeEnum = annotation;
                }
            }
            // 判断是否包含了当前终端编码
            if (Objects.nonNull(clientCodeEnum)){
                if (!CollUtil.toList(clientCodeEnum.value()).contains(clientCode)){
                    throw new RouterCheckException("该终端没有权限访问该路径");
                }
            }
        }
        return false;
    }
}
