package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchLicenseProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户营业执照信息
///
@Repository
public class MchLicenseProfileManager extends BaseManager<MchLicenseProfileMapper, MchLicenseProfile> {

    /// 根据商户编号查询商户营业执照信息
    public Optional<MchLicenseProfile> findByMchNo(String mchNo) {
        return findByField(MchLicenseProfile::getMchNo, mchNo);
    }
}
