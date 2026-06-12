package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchShopProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户经营场所信息
///
@Repository
public class MchShopProfileManager extends BaseManager<MchShopProfileMapper, MchShopProfile> {

    /// 根据商户编号查询商户经营场所信息
    public Optional<MchShopProfile> findByMchNo(String mchNo) {
        return findByField(MchShopProfile::getMchNo, mchNo);
    }
}