package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbLicenseProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件营业执照信息Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbLicenseProfileManager extends BaseManager<OnbLicenseProfileMapper, OnbLicenseProfile> {

    /// 根据申请ID查询营业执照信息
    public Optional<OnbLicenseProfile> findByApplyId(Long applyId) {
        return findByField(OnbLicenseProfile::getApplyId, applyId);
    }
}
