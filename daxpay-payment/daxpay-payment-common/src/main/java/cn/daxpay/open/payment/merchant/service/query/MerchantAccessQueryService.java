package cn.daxpay.open.payment.merchant.service.query;

import cn.daxpay.open.payment.merchant.dto.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.dto.MerchantAccessInfo;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户接入信息查询服务
///
/// 供支付总线层查询商户/应用的接入信息,返回精简 DTO,不暴露 JPA 实体。
@Service
@RequiredArgsConstructor
public class MerchantAccessQueryService {
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantCredentialService merchantCredentialService;

    /// 根据商户号查询商户接入信息
    public MerchantAccessInfo getMerchantByMchNo(String mchNo) {
        return merchantInfoManager.findByMchNo(mchNo)
                .map(this::toMerchantAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询默认应用
    public MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo) {
        return mchAppInfoManager.findDefaultByMchNoNotTenant(mchNo)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据应用号查询应用
    public MchAppInfoAccessInfo getAppByAppId(String appId) {
        return mchAppInfoManager.findByAppIdNotTenant(appId)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询商户公钥
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
