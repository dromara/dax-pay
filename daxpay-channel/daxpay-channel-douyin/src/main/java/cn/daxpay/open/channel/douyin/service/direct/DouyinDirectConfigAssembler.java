package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连通道凭证组装器
///
/// 从进件对象([DouyinDirectApp] + [DouyinDirectKeyConfig] + [DouyinDirectChannelMerchant])读取
/// appId / mchId / 私钥 / 证书序列号 / 加密密钥, 组装为下发给子应用的通道调用凭证 [DouyinSdkCredential]。
///
/// 应用解析优先级(委托 [DouyinDirectAppCapabilityService#resolveApp]):
/// 显式能力关联 > appType 自动推导 > 通道商户首个应用兜底。
///
/// 供抖音直连策略([cn.daxpay.open.channel.douyin.strategy.DouyinDirectPayStrategy] 等)组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectConfigAssembler {

    private final DouyinDirectAppCapabilityService appCapabilityService;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(兜底定位应用, 当前抖音 resolveApp 未使用, 保留对齐支付宝签名)
    /// @param channelMchNo 通道商户号(定位密钥/商户绑定/应用)
    /// @param capability   支付能力编码(用于选择匹配的应用)
    /// @return 抖音 SDK 凭证, 字段对齐子应用 DouyinSdkCredential
    public DouyinSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 解析支付使用的应用(显式能力关联 > appType 推导 > 通道商户首个兜底)
        DouyinDirectApp app = appCapabilityService.resolveApp(channelMchNo, capability)
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));

        // 2. 读取通道商户绑定(获取抖音商户号 dyMchId 作为 mchId)
        DouyinDirectChannelMerchant merchant = channelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 3. 读取密钥配置(私钥 / 证书序列号 / 加密密钥)
        DouyinDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 4. 组装凭证
        var credential = new DouyinSdkCredential();
        credential.setDouyinAppId(app.getDouyinAppId());
        credential.setMchId(merchant.getDyMchId());
        credential.setMerchantSerialNumber(keyConfig.getMerchantSerialNumber());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setEncryptKey(keyConfig.getEncryptKey());
        return credential;
    }
}
