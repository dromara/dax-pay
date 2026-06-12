package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchCardHolderProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 持卡人信息
///
@Repository
public class MchCardHolderProfileManager extends BaseManager<MchCardHolderProfileMapper, MchCardHolderProfile> {

    /// 根据商户编号查询持卡人信息
    public Optional<MchCardHolderProfile> findByMchNo(String mchNo) {
        return findByField(MchCardHolderProfile::getMchNo, mchNo);
    }
}