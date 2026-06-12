package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbBankCardProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件银行账户信息Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbBankCardProfileManager extends BaseManager<OnbBankCardProfileMapper, OnbBankCardProfile> {

    /// 根据申请ID查询银行账户信息
    public Optional<OnbBankCardProfile> findByApplyId(Long applyId) {
        return findByField(OnbBankCardProfile::getApplyId, applyId);
    }

}
