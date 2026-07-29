package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连通道凭证组装器
///
/// 从进件对象读取 appId / mchId / 私钥 / 证书序列号 / 加密密钥, 组装为下发给子应用的通道调用凭证 [DouyinSdkCredential]。
///
/// 应用解析(获取 douyinAppId)委托 [DouyinAppFacade#resolve]:显式 channelAppId → 通道能力绑 → appType 推导,
/// 应用主数据已上移至商户/平台级(dy_mch_app / dy_platform_app), 通道商户下不再持有抖音应用。
/// 密钥/证书(douyin_direct_key_config)与通道商户绑定(dyMchId)仍保留在通道商户维度。
///
/// 供抖音直连策略([cn.daxpay.open.channel.douyin.strategy.DouyinDirectPayStrategy] 等)组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectConfigAssembler {

    private final DouyinAppFacade douyinAppFacade;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(应用解析的商户档隔离条件)
    /// @param channelMchNo 通道商户号(定位密钥/商户绑定/能力绑)
    /// @param capability   支付能力编码(用于选择匹配的应用)
    /// @return 抖音 SDK 凭证, 字段对齐子应用 DouyinSdkCredential
    public DouyinSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 解析支付使用的应用(显式 channelAppId → 通道能力绑 → appType 推导；未命中 facade 自行报错)
        DyAppView app = douyinAppFacade.resolve(mchNo, channelMchNo, capability, null, null);

        // 2. 读取通道商户绑定(获取抖音商户号 dyMchId 作为 mchId)
        DouyinDirectChannelMerchant merchant = channelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 3. 读取密钥配置(私钥 / 证书序列号 / 加密密钥)
        DouyinDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 4. 组装凭证
        var credential = new DouyinSdkCredential();
        credential.setDouyinAppId(app.douyinAppId());
        credential.setMchId(merchant.getDyMchId());
        credential.setMerchantSerialNumber(keyConfig.getMerchantSerialNumber());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setEncryptKey(keyConfig.getEncryptKey());
        return credential;
    }
}
