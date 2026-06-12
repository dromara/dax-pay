package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbCardHolderProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件持卡人信息Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbCardHolderProfileManager extends BaseManager<OnbCardHolderProfileMapper, OnbCardHolderProfile> {

    /// 根据申请ID查询持卡人信息
    public Optional<OnbCardHolderProfile> findByApplyId(Long applyId) {
        return findByField(OnbCardHolderProfile::getApplyId, applyId);
    }

}
