package cn.daxpay.open.channel.union.service;

import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.entity.UnionKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取固化 sandbox 标识,
/// 从密钥配置([UnionKeyConfig])读取银联商户号(merId)与 RSA2 三证书,
/// 组装为下发给子应用的通道调用凭证 [UnionSdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionConfigAssembler {

    private final UnionKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;

    /// 组装商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(保留对齐签名, 银联不使用)
    /// @return 云闪付 SDK 凭证
    public UnionSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        boolean sandbox = channelMerchant.isSandbox();
        // 2. 密钥配置(银联商户号 merId + RSA2 三证书)
        UnionKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        // 3. 组装凭证
        var credential = new UnionSdkCredential();
        credential.setMerId(keyConfig.getMerId());
        credential.setSignType(keyConfig.getSignType());
        credential.setCertSign(keyConfig.isCertSign());
        credential.setKeyPrivateCert(keyConfig.getKeyPrivateCert());
        credential.setKeyPrivateCertPwd(keyConfig.getKeyPrivateCertPwd());
        credential.setAcpMiddleCert(keyConfig.getAcpMiddleCert());
        credential.setAcpRootCert(keyConfig.getAcpRootCert());
        credential.setSandbox(sandbox);
        return credential;
    }
}
