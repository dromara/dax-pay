package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchBaseProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户基础信息
///
@Repository
public class MchBaseProfileManager extends BaseManager<MchBaseProfileMapper, MchBaseProfile> {

    /// 根据商户编号查询商户基础信息
    public Optional<MchBaseProfile> findByMchNo(String mchNo) {
        return findByField(MchBaseProfile::getMchNo, mchNo);
    }
}
