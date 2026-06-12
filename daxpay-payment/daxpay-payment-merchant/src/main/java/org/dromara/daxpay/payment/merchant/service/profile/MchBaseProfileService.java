package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.convert.profile.MchBaseProfileConvert;
import org.dromara.daxpay.payment.merchant.dao.profile.MchBaseProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchBaseProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchBaseProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchBaseProfileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户基础资料服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchBaseProfileService {

    private final MchBaseProfileManager mchBaseProfileManager;

    /// 根据商户号查询基础资料
    public MchBaseProfileResult findByMchNo(String mchNo) {
        return mchBaseProfileManager.findByMchNo(mchNo)
                .map(MchBaseProfile::toResult)
                .orElse(null);
    }

    /// 保存基础资料
    @Transactional(rollbackFor = Exception.class)
    public void save(MchBaseProfileParam param) {
        String mchNo = param.getMchNo();
        MchBaseProfile profile = mchBaseProfileManager.findByMchNo(mchNo).orElse(null);
        if (profile == null) {
            profile = MchBaseProfileConvert.CONVERT.toEntity(param);
            profile.setMchNo(mchNo);
            mchBaseProfileManager.save(profile);
        } else {
            MchBaseProfileConvert.CONVERT.copy(param, profile);
            mchBaseProfileManager.updateById(profile);
        }
    }
}
