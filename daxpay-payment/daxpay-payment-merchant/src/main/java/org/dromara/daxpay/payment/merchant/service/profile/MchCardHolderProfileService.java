package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.dao.profile.MchCardHolderProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchCardHolderProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchCardHolderProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchCardHolderProfileResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户持卡人信息服务
///
@Service
@RequiredArgsConstructor
public class MchCardHolderProfileService {

    private final MchCardHolderProfileManager cardHolderProfileManager;

    /// 根据商户号查询持卡人信息
    public MchCardHolderProfileResult findByMchNo(String mchNo) {
        return cardHolderProfileManager.findByMchNo(mchNo)
                .map(MchCardHolderProfile::toResult)
                .orElse(null);
    }

    /// 保存或更新持卡人信息
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchCardHolderProfileParam param) {
        if (param.getMchNo() == null) {
            return;
        }
        MchCardHolderProfile entity = cardHolderProfileManager.findByMchNo(param.getMchNo())
                .orElseGet(() -> {
                    MchCardHolderProfile profile = new MchCardHolderProfile();
                    profile.setMchNo(param.getMchNo());
                    return profile;
                });
        BeanUtil.copyProperties(param, entity, "mchNo");
        cardHolderProfileManager.saveOrUpdate(entity);
    }
}
