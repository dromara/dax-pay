package cn.daxpay.open.channel.leshua.service.isv;

import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvChannelMerchantManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 乐刷服务商通道凭证组装器
///
/// 从服务商密钥配置([LeshuaIsvKeyConfig]) + 通道商户绑定([LeshuaIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起乐刷 API 调用。
///
/// 字段映射(对齐乐刷交易接口):
/// - merchant_id ← [LeshuaIsvKeyConfig#lsMchNo](服务商级商户号, 全局唯一)
/// - tradeKey ← [LeshuaIsvKeyConfig#tradeKey]
/// - signType ← [LeshuaIsvKeyConfig#signType]
///
/// 注: 乐刷交易接口只需 merchant_id + tradeKey, 子商户号(merchant_id)由服务商全局配置提供。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaIsvConfigAssembler {

    private final LeshuaIsvChannelMerchantManager leshuaIsvChannelMerchantManager;
    private final LeshuaIsvKeyConfigService leshuaIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装乐刷通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号
    /// @param channelMchNo 通道商户号(乐刷商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 乐刷不按能力路由)
    /// @return 乐刷 SDK 凭证
    public LeshuaSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 沙箱状态读取支付产品配置的生效环境
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.LESHUA_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        // 服务商密钥(按生效环境取对应环境密钥, 含 lsMchNo + tradeKey + signType)
        LeshuaIsvKeyConfig keyConfig = leshuaIsvKeyConfigService.getByProductForPay(ProductEnum.LESHUA_PAY.getCode(), sandbox);
        // 通道商户绑定(校验环境一致性)
        LeshuaIsvChannelMerchant channelMerchant = leshuaIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 乐刷: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 环境一致性校验
        if (channelMerchant.isSandbox() != sandbox) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.envMismatch");
        }

        LeshuaSdkCredential credential = new LeshuaSdkCredential();
        credential.setLsMchNo(keyConfig.getLsMchNo());
        credential.setTradeKey(keyConfig.getTradeKey());
        credential.setSignType(keyConfig.getSignType());
        credential.setSandbox(sandbox);
        return credential;
    }
}
