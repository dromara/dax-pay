package cn.daxpay.open.channel.stripe.service;

import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.dao.StripeKeyConfigManager;
import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 通道凭证组装器
///
/// 从进件对象读取 secretKey / publishableKey / webhookSecret, 组装为下发给子应用的通道调用凭证 [StripeSdkCredential]。
/// Stripe 无"应用"概念, 一个账户一套密钥, 直接按通道商户号读取密钥配置即可。
/// 测试/生产环境由密钥前缀(sk_test_/sk_live_)决定, 子应用 Stripe SDK 据此自判, 无需 sandbox 标识。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeConfigAssembler {

    private final StripeKeyConfigManager keyConfigManager;

    /// 组装商户的通道调用凭证(下发给子应用)
    ///
    /// @param channelMchNo 通道商户号(定位密钥)
    /// @return Stripe SDK 凭证, 字段对齐子应用 StripeSdkCredential
    public StripeSdkCredential buildConfig(String channelMchNo) {
        StripeKeyConfig keyConfig = keyConfigManager.findByChannelMchNo(channelMchNo)
                // 通道商户的密钥配置不存在
                .orElseThrow(() -> new RuntimeException("Stripe 密钥配置不存在: " + channelMchNo));
        var credential = new StripeSdkCredential();
        credential.setSecretKey(keyConfig.getSecretKey());
        credential.setPublishableKey(keyConfig.getPublishableKey());
        credential.setWebhookSecret(keyConfig.getWebhookSecret());
        return credential;
    }
}
