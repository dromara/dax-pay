package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.core.annotation.InternalPath;
import cn.daxpay.open.platform.core.exception.BizWarnException;
import cn.daxpay.open.platform.capability.auth.service.RouterCheck;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 内部接口鉴权处理
///
/// @see InternalPath
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
                // 判断当前用户是否为超级管理员
                if (!SecurityUtil.getUser().isAdmin()){
                    // 通用: 内部接口不允许普通用户进行调用
                    throw new BizWarnException(CommonCode.FAIL_CODE, "error.common.internalForbidden");
                }
            }
        }
        return false;
    }

}
