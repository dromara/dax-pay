package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbLegalProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件法人信息Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbLegalProfileManager extends BaseManager<OnbLegalProfileMapper, OnbLegalProfile> {

    /// 根据申请ID查询法人信息
    public Optional<OnbLegalProfile> findByApplyId(Long applyId) {
        return findByField(OnbLegalProfile::getApplyId, applyId);
    }

}
