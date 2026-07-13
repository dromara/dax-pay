package cn.daxpay.open.channel.yeepay.service.direct;

import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.entity.direct.YeepayDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product, 从直连配置([YeepayDirectKeyConfig])读取
/// 商户身份(merchantNo/yopIsvNo) 与密钥(appKey/privateKey/yopPublicKey/wxAppId/wxAppSecret),
/// 组装为下发给子应用的通道调用凭证 [YeepaySdkCredential]。
///
/// 沙箱状态不挂在商户配置上, 运行时读取支付产品配置([PayProductConfig] 的 activeEnv)判断。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayDirectConfigAssembler {

    private final YeepayDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(易宝不使用, 保留对齐签名)
    /// @return 易宝 SDK 凭证
    public YeepaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(获取 product, 用于查支付产品配置的沙箱环境)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 2. 沙箱状态读取支付产品配置的生效环境(先定环境, 再查对应环境密钥)
        boolean sandbox = payProductConfigManager.findByProduct(channelMerchant.getProduct())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);

        // 3. 直连配置(商户身份 + 密钥, 按沙箱环境取对应密钥)
        YeepayDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);

        // 4. 组装凭证
        var credential = new YeepaySdkCredential();
        credential.setMerchantNo(keyConfig.getMerchantNo());
        credential.setYopIsvNo(keyConfig.getYopIsvNo());
        credential.setAppKey(keyConfig.getAppKey());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setYopPublicKey(keyConfig.getYopPublicKey());
        credential.setWxAppId(keyConfig.getWxAppId());
        credential.setWxAppSecret(keyConfig.getWxAppSecret());
        credential.setSandbox(sandbox);
        return credential;
    }
}
