package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbBaseProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件商户信息
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbMerchantProfileManager extends BaseManager<OnbMerchantProfileMapper, OnbBaseProfile> {

    /// 根据申请ID查询商户信息
    public Optional<OnbBaseProfile> findByApplyId(Long applyId) {
        return findByField(OnbBaseProfile::getApplyId, applyId);
    }

}
