package org.dromara.daxpay.payment.common.handler;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.bootx.platform.starter.auth.exception.RouterCheckException;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;
import java.util.Optional;

/**
 * 支付终端路径过滤和简化方式权限判断
 * @author xxm
 * @since 2025/1/31
 */
@Component
@RequiredArgsConstructor
public class DaxpayClientRouterCheck implements RouterCheck {
    private final ClientCodeService clientCodeService;
    private final UserRoleService userRoleService;

    @Override
    public int sortNo() {
        return -10;
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
                // 方法上上是否加了终端标识注解
                clientCodeEnum = handlerMethod.getMethodAnnotation(ClientCode.class);
            }
            else {
                // controller和方法上都加了终端注解,以方法上为准
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
                // 如果属于支付终端路径, 通过简化直接检查拥有对应角色是否拥有权限
                return this.checkUserDaxpayRole(clientCode);
            }
        }
        return false;
    }

    /**
     * 检查用户角色
     */
    private boolean checkUserDaxpayRole(String clientCode) {
        Optional<UserDetail> optional = SecurityUtil.getCurrentUser();
        if (optional.isEmpty()){
            return false;
        }
        var userDetail = optional.get();
        var roles = userRoleService.findRolesByUser(userDetail.getId());
        var roleCodes = roles.stream()
                .map(RoleResult::getCode)
                .toList();
        // 商户端
        if (Objects.equals(clientCode, DaxPayCode.Client.MERCHANT)){
            return roleCodes.contains(DaxPayCode.Role.MERCHANT_ADMIN);
        }
        // 运营端
        else if (Objects.equals(clientCode, DaxPayCode.Client.ADMIN)){
            return roleCodes.contains(DaxPayCode.Role.ISV_ADMIN);
        }
        return false;
    }
}
