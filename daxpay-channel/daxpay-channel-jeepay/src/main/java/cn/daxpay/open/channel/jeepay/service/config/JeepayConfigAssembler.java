package cn.daxpay.open.channel.jeepay.service.config;

import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.entity.JeepayKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay通道凭证组装器
///
/// 从通道密钥配置([JeepayKeyConfig], 按通道商户号定位)组装通道调用凭证 [JeepaySdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起 Jeepay API 调用。
///
/// Jeepay 一通道一产品(jee_pay 商户模式), 凭证不携带沙箱标记(不提供集测环境);
/// 为三方聚合通道, 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.jeepay.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayConfigAssembler {

    private final JeepayKeyConfigService jeepayKeyConfigService;

    /// 组装 Jeepay 通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(Jeepay 不按能力路由, 保留对齐签名)
    /// @return Jeepay SDK 凭证
    public JeepaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(平台地址/商户身份 + 应用密钥, 关键字段缺失 fail-fast)
        JeepayKeyConfig keyConfig = jeepayKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        JeepaySdkCredential credential = new JeepaySdkCredential();
        credential.setServerUrl(keyConfig.getServerUrl());
        credential.setMchNo(keyConfig.getJeepayMchNo());
        credential.setAppId(keyConfig.getAppId());
        credential.setAppSecret(keyConfig.getAppSecret());
        return credential;
    }
}
