package org.dromara.daxpay.payment.merchant.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchBankCardProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户银行卡信息
///
@Repository
public class MchBankCardProfileManager extends BaseManager<MchBankCardProfileMapper, MchBankCardProfile> {

    /// 根据商户编号查询商户银行卡信息
    public Optional<MchBankCardProfile> findByMchNo(String mchNo) {
        return findByField(MchBankCardProfile::getMchNo, mchNo);
    }
}