package cn.daxpay.open.payment.merchant.service.access;

import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.common.access.MerchantAccessInfo;
import cn.daxpay.open.payment.common.access.MerchantAccessPort;
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
/// [MerchantAccessPort] 在 merchant 侧的实现：持有 DAO，返回精简 Access DTO。
/// 引导类读路径统一走 `*NotTenant`；`initMch` 之后的业务读（如公钥验签）走租户内查询。
///
/// 配置态 CRUD 请使用各业务 Service，勿在此扩展管理端能力。
/// 横切调用方应依赖 [MerchantAccessPort]，勿直接依赖本实现类。
@Service
@RequiredArgsConstructor
public class MerchantAccessQueryService implements MerchantAccessPort {
    private final MerchantInfoManager merchantInfoManager;

    private final MchAppInfoManager mchAppInfoManager;

    private final MerchantCredentialManager merchantCredentialManager;

    /// 根据商户号查询商户接入信息（引导用，忽略租户）
    @Override
    public MerchantAccessInfo getMerchantByMchNo(String mchNo) {
        return merchantInfoManager.findByMchNoNotTenant(mchNo)
                .map(this::toMerchantAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询默认应用（引导用，忽略租户）
    @Override
    public MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo) {
        return mchAppInfoManager.findDefaultByMchNoNotTenant(mchNo)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据应用号查询应用（引导用，忽略租户）
    @Override
    public MchAppInfoAccessInfo getAppByAppId(String appId) {
        return mchAppInfoManager.findByAppIdNotTenant(appId)
                .map(this::toMchAppInfoAccessInfo)
                .orElse(null);
    }

    /// 根据商户号查询商户公钥
    ///
    /// 调用方须已完成 `initMch`（或回调控制器已 `bindMchNoForCallback`），走**租户内**查询。
    @Override
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
