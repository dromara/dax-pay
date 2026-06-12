package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.convert.profile.MchShopProfileConvert;
import org.dromara.daxpay.payment.merchant.dao.profile.MchShopProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchShopProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchShopProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchShopProfileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户经营场所信息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchShopProfileService {

    private final MchShopProfileManager mchShopProfileManager;

    /// 根据商户号查询经营场所信息
    public MchShopProfileResult findByMchNo(String mchNo) {
        return mchShopProfileManager.findByMchNo(mchNo)
                .map(MchShopProfile::toResult)
                .orElse(null);
    }

    /// 保存或更新经营场所信息
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchShopProfileParam param) {
        String mchNo = param.getMchNo();
        MchShopProfile profile = mchShopProfileManager.findByMchNo(mchNo).orElse(null);
        if (profile == null) {
            profile = MchShopProfileConvert.CONVERT.toEntity(param);
            profile.setMchNo(mchNo);
            mchShopProfileManager.save(profile);
        } else {
            MchShopProfileConvert.CONVERT.copy(param, profile);
            mchShopProfileManager.updateById(profile);
        }
    }
}
