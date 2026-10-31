package cn.daxpay.open.payment.common.handler;

import cn.daxpay.open.platform.capability.auth.authentication.UserTerminateHook;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/// # 用户注销挂靠钩子实现(payment 域)
///
/// 汇总用户在商户([MerchantUser])挂靠表的关联描述,
/// 供 iam 侧注销入口校验(挂靠非空拒删); 模式对齐 [DaxUserInfoStatusCheck]。
/// 开源版无代理商域, 挂靠域仅商户一处。
@Component
@RequiredArgsConstructor
public class DaxUserTerminateHook implements UserTerminateHook {

    private final MerchantUserManager merchantUserManager;

    @Override
    public List<String> findAffiliations(Long userId) {
        List<String> affiliations = new ArrayList<>();
        merchantUserManager.findAllByField(MerchantUser::getUserId, userId)
                .forEach(user -> affiliations.add("商户 " + user.getMchNo()));
        return affiliations;
    }
}
