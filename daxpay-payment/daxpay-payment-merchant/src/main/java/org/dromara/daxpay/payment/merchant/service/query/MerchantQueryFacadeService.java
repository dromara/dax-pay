package org.dromara.daxpay.payment.merchant.service.query;

import org.dromara.daxpay.payment.common.service.MerchantContextQueryService;
import org.dromara.daxpay.payment.common.service.MerchantPaymentQueryService;
import org.dromara.daxpay.payment.common.service.dto.MchAppInfoAccessInfo;
import org.dromara.daxpay.payment.common.service.dto.MerchantAccessInfo;
import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantInfoManager;
import org.dromara.daxpay.payment.merchant.entity.appinfo.MchAppInfo;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.merchant.service.config.MerchantCredentialService;
import org.dromara.daxpay.payment.merchant.service.user.MerchantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户公共查询门面
///
@Service
@RequiredArgsConstructor
public class MerchantQueryFacadeService implements MerchantContextQueryService, MerchantPaymentQueryService {
    private final MerchantUserService merchantUserService;
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantCredentialService merchantCredentialService;

    @Override
    public String findMchNoByUserId(Long userId) {
        return merchantUserService.findByUserId(userId);
    }

    @Override
    public MerchantAccessInfo getMerchantByMchNo(String mchNo) {
        return merchantInfoManager.findByMchNo(mchNo)
                .map(this::toMerchantAccessInfo)
                .orElse(null);
    }

    @Override
    public MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo) {
        return mchAppInfoManager.findDefaultByMchNoNotTenant(mchNo)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    @Override
    public MchAppInfoAccessInfo getAppByAppId(String appId) {
        return mchAppInfoManager.findByAppIdNotTenant(appId)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    @Override
    public String findMerchantPublicKey(String mchNo) {
        return merchantCredentialService.findByMchNo(mchNo).getPublicKey();
    }

    private MerchantAccessInfo toMerchantAccessInfo(MerchantInfo merchantInfo) {
        return new MerchantAccessInfo()
                .setMchNo(merchantInfo.getMchNo())
                .setStatus(merchantInfo.getStatus())
                .setIsvNo(merchantInfo.getIsvNo());
    }

    private MchAppInfoAccessInfo toMchAppInfoAccessInfo(MchAppInfo mchApp) {
        return new MchAppInfoAccessInfo()
                .setAppId(mchApp.getAppId())
                .setMchNo(mchApp.getMchNo())
                .setStatus(mchApp.getStatus());
    }
}
