package cn.daxpay.open.channel.helipay.service.config;

import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.entity.HelipayKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝通道凭证组装器
///
/// 从通道密钥配置([HelipayKeyConfig], 按通道商户号定位)组装通道调用凭证 [HelipaySdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起合利宝扫码产品线调用。
///
/// 合利宝一通道一产品(heli_pay 直连商户模式), 凭证不携带沙箱标记(无独立沙箱网关);
/// 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.helipay.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayConfigAssembler {

    private final HelipayKeyConfigService helipayKeyConfigService;

    /// 组装合利宝通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(合利宝不按能力路由, 保留对齐签名)
    /// @return 合利宝 SDK 凭证
    public HelipaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(商户身份 + 签名密钥, 关键字段缺失 fail-fast)
        HelipayKeyConfig keyConfig = helipayKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        HelipaySdkCredential credential = new HelipaySdkCredential();
        credential.setCustomerNumber(keyConfig.getCustomerNumber());
        credential.setSubCustomerNumber(keyConfig.getSubCustomerNumber());
        credential.setSignKey(keyConfig.getSignKey());
        credential.setPublicAppId(keyConfig.getPublicAppId());
        credential.setH5AppId(keyConfig.getH5AppId());
        credential.setReportId(keyConfig.getReportId());
        return credential;
    }
}
