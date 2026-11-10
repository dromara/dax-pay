package cn.daxpay.open.payment.admin.service.dashboard;

import cn.daxpay.open.payment.admin.result.dashboard.AdminMchBaseCountResult;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.dao.wxverify.WxDomainVerifyManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户工作台基础计数服务(运营端)
///
/// 与 [AdminDashboardDeviceService] 同构, 商户号由运营端显式指定(跨商户查看),
/// 计数一律按 mchNo 显式过滤(运营端无商户上下文, 租户拦截整体放开)。
@Service
@RequiredArgsConstructor
public class AdminDashboardMerchantService {

    private final MchStoreInfoManager mchStoreInfoManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final MerchantUserManager merchantUserManager;
    private final WxDomainVerifyManager wxDomainVerifyManager;
    private final MerchantCredentialManager merchantCredentialManager;

    /// 指定商户的五类基础计数(用户/门店/应用/通道商户/域名校验) + 对接凭证配置状态
    ///
    /// 计数口径: 一律存量总数, 只过滤逻辑删除, 不按启用/状态过滤;
    /// 用户关系表无逻辑删除字段, 行数即用户数(含停用账号)。
    /// 凭证状态走 manager 只读查询, 不复用 [MerchantCredentialService#findByMchNo](无记录时会自动建空行)。
    public AdminMchBaseCountResult counts(String mchNo) {
        AdminMchBaseCountResult result = new AdminMchBaseCountResult();
        // 商户用户数(用户-商户关联关系存量)
        result.setMerchantUserCount((int) merchantUserManager.countByMchNo(mchNo));
        // 名下门店数
        result.setStoreCount((int) mchStoreInfoManager.countByMchNo(mchNo));
        // 名下应用数
        result.setAppCount((int) mchAppInfoManager.countByMchNo(mchNo));
        // 名下通道商户数
        result.setChannelMerchantCount((int) channelMerchantManager.countByMchNo(mchNo));
        // 名下域名校验文件数(平台级不计入)
        result.setWxVerifyCount((int) wxDomainVerifyManager.countByMchNo(mchNo));
        // 对接凭证配置状态(字段级, 口径同凭证配置页; 无记录即未配置)
        MerchantCredential credential = merchantCredentialManager.findByMchNo(mchNo).orElse(null);
        result.setPublicKeyConfigured(MerchantCredentialService.isPublicKeyConfigured(credential));
        result.setSecretKeyConfigured(MerchantCredentialService.isSecretKeyConfigured(credential));
        result.setCredentialConfigured(MerchantCredentialService.isConfigured(credential));
        return result;
    }
}
