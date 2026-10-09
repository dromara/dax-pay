package cn.daxpay.open.payment.common.handler;

import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.platform.capability.auth.authentication.SubjectAdminGuardHook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/// # 主体管理员保护钩子实现(payment 域)
///
/// 查 [MerchantUser] 挂靠表的 administrator 标识,
/// 供 iam 侧角色分配与封禁/锁定入口拒绝主体管理员被改角色/被封禁。
@Component
@RequiredArgsConstructor
public class DaxSubjectAdminGuardHook implements SubjectAdminGuardHook {

    private final MerchantUserManager merchantUserManager;

    /// 返回纯主体标识(商户号)供 i18n 消息占位, 不夹文案前缀(避免非中文语言混入中文)
    @Override
    public String findSubjectAdmin(Long userId) {
        Optional<MerchantUser> merchantAdmin = merchantUserManager.findAllByField(MerchantUser::getUserId, userId).stream()
                .filter(MerchantUser::isAdministrator)
                .findFirst();
        return merchantAdmin.map(MerchantUser::getMchNo).orElse(null);
    }
}
