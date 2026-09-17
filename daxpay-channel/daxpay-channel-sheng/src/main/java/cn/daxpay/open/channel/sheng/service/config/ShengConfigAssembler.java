package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通通道凭证组装器
///
/// 从通道密钥配置([ShengKeyConfig], 按通道商户号定位)组装通道调用凭证 [ShengSdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起盛付通 API 调用。
///
/// 盛付通分商户(sheng_pay)/服务商(sheng_isv)双产品, 本组装器仅服务商户模式(服务商凭证见 [ShengIsvConfigAssembler]);
/// 无沙箱双环境(官方不提供集测接口), 凭证不携带沙箱标记。
/// 盛付通为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.sheng.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengConfigAssembler {

    private final ShengKeyConfigService shengKeyConfigService;

    /// 组装盛付通通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(盛付通不按能力路由, 保留对齐签名)
    /// @return 盛付通 SDK 凭证
    public ShengSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(商户身份 mchId/sdpAppId + 签名密钥对, 关键字段缺失 fail-fast)
        ShengKeyConfig keyConfig = shengKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        ShengSdkCredential credential = new ShengSdkCredential();
        credential.setMchId(keyConfig.getShengMchId());
        credential.setSdpAppId(keyConfig.getSdpAppId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setShengpayPublicKey(keyConfig.getShengpayPublicKey());
        return credential;
    }
}
