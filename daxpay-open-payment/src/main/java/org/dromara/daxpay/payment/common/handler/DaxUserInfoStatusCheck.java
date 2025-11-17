package org.dromara.daxpay.payment.common.handler;

import cn.bootx.platform.iam.service.client.ClientCodeService;
import cn.bootx.platform.starter.auth.authentication.UserInfoStatusCheck;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.enums.MerchantStatusEnum;
import org.dromara.daxpay.payment.merchant.service.info.MerchantUserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * DaxPay登录验证处理，没有对应终端的权限不运行登录
 * @author xxm
 * @since 2025/8/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DaxUserInfoStatusCheck implements UserInfoStatusCheck {
    private final MerchantUserAdminService merchantUserService;
    private final MerchantManager merchantManager;
    private final IsvInfoManager isvInfoManager;
    private final ClientCodeService clientCodeService;

    /**
     * 检查用户是否拥有当前终端的权限
     *
     * @param authInfoResult 认证返回结果
     * @param context        登录认证上下文
     */
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        // 判断终端
        String clientCode = clientCodeService.getClientCode();
        // 商户端
        if (Objects.equals(clientCode, DaxPayCode.Client.MERCHANT)){
            String merchantNo = Optional.ofNullable(merchantUserService.findByUserId(authInfoResult.getUserDetail()
                            .getId()))
                    .orElseThrow(() -> new LoginFailureException("您没有商户端的登录权限"));
            Merchant merchant = merchantManager.findByMchNo(merchantNo)
                    .orElseThrow(() -> new LoginFailureException("您没有商户端的登录权限"));
            if (Objects.equals(merchant.getStatus(), MerchantStatusEnum.DISABLED.getCode())){
                throw new LoginFailureException("该商户已禁用");
            }
            var isvInfo = isvInfoManager.findByIsvNo(merchant.getIsvNo())
                    .orElseThrow(() -> new LoginFailureException("您没有代理端的登录权限"));
            if (Objects.equals(isvInfo.getStatus(), IsvStatusEnum.DISABLED.getCode())){
                throw new LoginFailureException("所属服务商已禁用");
            }
        } else {
            // 运营端
            String merchant = merchantUserService.findByUserId(authInfoResult.getUserDetail().getId());
            if (merchant != null){
                throw new LoginFailureException("您没有运营端的权限，请使用商户端登录");
            }
        }
    }
}
