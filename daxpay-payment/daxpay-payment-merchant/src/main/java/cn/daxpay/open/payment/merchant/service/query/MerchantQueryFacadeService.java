package cn.daxpay.open.payment.merchant.service.query;

import cn.daxpay.open.payment.common.service.MerchantContextQueryService;
import cn.daxpay.open.payment.common.service.MerchantPaymentQueryService;
import cn.daxpay.open.payment.common.service.dto.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.common.service.dto.MerchantAccessInfo;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserService;
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
                .setStatus(merchantInfo.getStatus());
    }

    private MchAppInfoAccessInfo toMchAppInfoAccessInfo(MchAppInfo mchApp) {
        return new MchAppInfoAccessInfo()
                .setAppId(mchApp.getAppId())
                .setMchNo(mchApp.getMchNo())
                .setStatus(mchApp.getStatus());
    }
}
