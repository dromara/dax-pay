package cn.daxpay.open.payment.merchant.service.query;

import cn.daxpay.open.payment.merchant.dto.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.dto.MerchantAccessInfo;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户接入信息查询服务（运行态 Access）
///
/// 供支付总线层在**商户上下文尚未装载**时查询商户/应用接入信息，返回精简 DTO。
/// 引导类读路径统一走 `*NotTenant`；`initMch` 之后的业务读（如公钥验签）走租户内查询。
///
/// 配置态 CRUD 请使用各业务 Service，勿在此扩展管理端能力。
@Service
@RequiredArgsConstructor
public class MerchantAccessQueryService {
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantCredentialManager merchantCredentialManager;

    /// 根据商户号查询商户接入信息（引导用，忽略租户）
    public MerchantAccessInfo getMerchantByMchNo(String mchNo) {
        return merchantInfoManager.findByMchNoNotTenant(mchNo)
                .map(this::toMerchantAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询默认应用（引导用，忽略租户）
    public MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo) {
        return mchAppInfoManager.findDefaultByMchNoNotTenant(mchNo)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据应用号查询应用（引导用，忽略租户）
    public MchAppInfoAccessInfo getAppByAppId(String appId) {
        return mchAppInfoManager.findByAppIdNotTenant(appId)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询商户公钥
    ///
    /// 调用方须已完成 `initMch`（或回调 Filter 已写入 mchNo），走**租户内**查询。
    public String findMerchantPublicKey(String mchNo) {
        return merchantCredentialManager.findByMchNo(mchNo)
                .map(MerchantCredential::getPublicKey)
                .orElse(null);
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
