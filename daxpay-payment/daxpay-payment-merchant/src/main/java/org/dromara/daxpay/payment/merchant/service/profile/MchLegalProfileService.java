package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.dao.profile.MchLegalProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchLegalProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchLegalProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchLegalProfileResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户法人信息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchLegalProfileService {

    private final MchLegalProfileManager legalProfileManager;

    /// 根据商户号查询法人信息
    public MchLegalProfileResult findByMchNo(String mchNo) {
        return legalProfileManager.findByMchNo(mchNo)
                .map(MchLegalProfile::toResult)
                .orElse(null);
    }

    /// 保存或更新法人信息
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchLegalProfileParam param) {
        if (param.getMchNo() == null) {
            return;
        }
        MchLegalProfile entity = legalProfileManager.findByMchNo(param.getMchNo())
                .orElseGet(() -> {
                    MchLegalProfile profile = new MchLegalProfile();
                    profile.setMchNo(param.getMchNo());
                    return profile;
                });
        BeanUtil.copyProperties(param, entity, "mchNo");
        legalProfileManager.saveOrUpdate(entity);
    }
}
