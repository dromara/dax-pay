package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.capability.auth.service.RouterCheck;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.util.PermCodeUtil;
import cn.daxpay.open.platform.iam.service.upms.UserRolePremService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Optional;

/// # 基于权限码注解的请求路由校验器。
///
/// 该校验器会在请求进入控制器前，读取控制器类或方法上的 {@link cn.daxpay.open.platform.core.annotation.PermCode} 注解，
/// 然后根据当前登录用户拥有的权限码集合决定是否允许访问目标接口。
/// 校验使用与扫描服务一致的完整权限码（menuCode:code）。
@Slf4j
@Component
@RequiredArgsConstructor
public class PermCodeRouterCheck implements RouterCheck {

    private final UserRolePremService userRolePremService;

    /// 执行路由权限校验。
    /// 仅对 {@link HandlerMethod} 进行处理，其他类型的 handler 直接返回不通过。
    @Override
    public boolean check(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return this.checkPermCode(handlerMethod);
        }
        return false;
    }

    /// 根据权限码注解执行鉴权判断。
    /// 未声明有效完整权限码、用户未登录，都会视为校验失败。
    private boolean checkPermCode(HandlerMethod handlerMethod) {
        PermCode classPermCode = handlerMethod.getBeanType().getAnnotation(PermCode.class);
        PermCode methodPermCode = handlerMethod.getMethodAnnotation(PermCode.class);
        if (classPermCode == null && methodPermCode == null) {
            return false;
        }
        String fullCode = PermCodeUtil.resolveFullCode(classPermCode, methodPermCode);
        if (StrUtil.isBlank(fullCode)) {
            return false;
        }
        Optional<UserDetail> userDetailOpt = SecurityUtil.getCurrentUser();
        if (userDetailOpt.isEmpty()) {
            return false;
        }
        List<String> userPermCodes = userRolePremService.findAllCodesByUser(userDetailOpt.get().getId());
        return userPermCodes.contains(fullCode);
    }

}
