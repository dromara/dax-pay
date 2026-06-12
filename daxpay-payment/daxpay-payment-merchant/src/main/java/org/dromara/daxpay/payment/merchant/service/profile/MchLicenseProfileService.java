package org.dromara.daxpay.payment.merchant.service.profile;

import org.dromara.daxpay.payment.merchant.dao.profile.MchLicenseProfileManager;
import org.dromara.daxpay.payment.merchant.entity.profile.MchLicenseProfile;
import org.dromara.daxpay.payment.merchant.param.profile.MchLicenseProfileParam;
import org.dromara.daxpay.payment.merchant.result.profile.MchLicenseProfileResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户营业执照信息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchLicenseProfileService {

    private final MchLicenseProfileManager licenseProfileManager;

    /// 根据商户号查询营业执照信息
    public MchLicenseProfileResult findByMchNo(String mchNo) {
        return licenseProfileManager.findByMchNo(mchNo)
                .map(MchLicenseProfile::toResult)
                .orElse(null);
    }

    /// 保存或更新营业执照信息
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchLicenseProfileParam param) {
        if (param.getMchNo() == null) {
            return;
        }
        MchLicenseProfile entity = licenseProfileManager.findByMchNo(param.getMchNo())
                .orElseGet(() -> {
                    MchLicenseProfile profile = new MchLicenseProfile();
                    profile.setMchNo(param.getMchNo());
                    return profile;
                });
        BeanUtil.copyProperties(param, entity, "mchNo");
        licenseProfileManager.saveOrUpdate(entity);
    }
}
