package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.dao.profile.MchBankCardProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchBankCardProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchBankCardProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBankCardProfileResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户银行卡信息服务
///
@Service
@RequiredArgsConstructor
public class MchBankCardProfileService {

    private final MchBankCardProfileManager bankCardProfileManager;

    /// 根据商户号查询银行卡信息
    public MchBankCardProfileResult findByMchNo(String mchNo) {
        return bankCardProfileManager.findByMchNo(mchNo)
                .map(MchBankCardProfile::toResult)
                .orElse(null);
    }

    /// 保存或更新银行卡信息
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchBankCardProfileParam param) {
        if (param.getMchNo() == null) {
            return;
        }
        MchBankCardProfile entity = bankCardProfileManager.findByMchNo(param.getMchNo())
                .orElseGet(() -> {
                    MchBankCardProfile profile = new MchBankCardProfile();
                    profile.setMchNo(param.getMchNo());
                    return profile;
                });
        BeanUtil.copyProperties(param, entity, "mchNo");
        bankCardProfileManager.saveOrUpdate(entity);
    }
}
