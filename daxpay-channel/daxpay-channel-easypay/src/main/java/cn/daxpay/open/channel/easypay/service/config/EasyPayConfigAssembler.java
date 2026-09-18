package cn.daxpay.open.channel.easypay.service.config;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付通道凭证组装器
///
/// 从通道密钥配置([EasyPayKeyConfig], 按通道商户号定位)组装通道调用凭证 [EasyPaySdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起易支付 API 调用。
///
/// 易支付一通道一产品(easy_pay 商户模式), 凭证不携带沙箱标记(不提供集测环境);
/// 为三方聚合通道, 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.easypay.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayConfigAssembler {

    private final EasyPayKeyConfigService easyPayKeyConfigService;

    /// 组装易支付通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(易支付不按能力路由, 保留对齐签名)
    /// @return 易支付 SDK 凭证
    public EasyPaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(平台地址/商户身份 + 签名密钥对, 关键字段缺失 fail-fast)
        EasyPayKeyConfig keyConfig = easyPayKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        EasyPaySdkCredential credential = new EasyPaySdkCredential();
        credential.setServerUrl(keyConfig.getServerUrl());
        credential.setPartnerId(keyConfig.getPartnerId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setPlatformPublicKey(keyConfig.getPlatformPublicKey());
        return credential;
    }
}
