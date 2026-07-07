package cn.daxpay.open.channel.adapay.service.direct;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付天下直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product, 从直连配置([AdapayDirectKeyConfig])读取
/// 汇付应用 ID 与签名密钥(apiKey/privateKey/publicKey), 组装为下发给子应用的通道调用凭证 [AdapaySdkCredential]。
///
/// 沙箱状态不挂在商户配置上, 运行时读取支付产品配置([PayProductConfig] 的 activeEnv)判断。
///
/// 汇付为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectConfigAssembler {

    private final AdapayDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(汇付不使用, 保留对齐签名)
    /// @return 汇付天下 SDK 凭证
    public AdapaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(获取 product, 用于查支付产品配置的沙箱环境)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 2. 直连配置(汇付应用ID + 签名密钥)
        AdapayDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 3. 沙箱状态读取支付产品配置的生效环境(不挂在商户配置上)
        boolean sandbox = payProductConfigManager.findByProduct(channelMerchant.getProduct())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);

        // 4. 组装凭证
        var credential = new AdapaySdkCredential();
        credential.setAdapayAppId(keyConfig.getAdapayAppId());
        credential.setApiKey(keyConfig.getApiKey());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        return credential;
    }
}
