package cn.daxpay.open.channel.hmpay.service.isv;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvChannelMerchantManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
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

/// # 河马付服务商通道凭证组装器
///
/// 从服务商密钥配置([HmpayIsvKeyConfig]) + 通道商户绑定([HmpayIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起河马付(杉德) API 调用。
///
/// 字段映射:
/// - sandAppId/privateKey/publicKey ← [HmpayIsvKeyConfig](服务商级, 全局唯一)
/// - merchantNo/storeId ← [HmpayIsvChannelMerchant](商户级)
///
/// 供支付策略([cn.daxpay.open.channel.hmpay.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvConfigAssembler {

    private final HmpayIsvChannelMerchantManager hmpayIsvChannelMerchantManager;
    private final HmpayIsvKeyConfigService hmpayIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装河马付通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(河马付商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 河马付不按能力路由)
    /// @return 河马付 SDK 凭证
    public HmpaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 先读取支付产品配置的生效环境判断 sandbox, 再按 sandbox 查对应环境的密钥
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.HM_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        // 服务商密钥(按 sandbox 分环境, 含 sandAppId/密钥; 缺失或关键字段为空时 fail-fast)
        HmpayIsvKeyConfig keyConfig = hmpayIsvKeyConfigService.getByProductForPay(ProductEnum.HM_PAY.getCode(), sandbox);
        // 通道商户绑定(取 merchantNo/storeId)
        HmpayIsvChannelMerchant channelMerchant = hmpayIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 河马付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("payment.error.channel.channelMerchantNotExist"));

        // 环境一致性校验
        if (channelMerchant.isSandbox() != sandbox) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.envMismatch");
        }

        HmpaySdkCredential credential = new HmpaySdkCredential();
        // 服务商身份与密钥
        credential.setSandAppId(keyConfig.getSandAppId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setMerchantNo(channelMerchant.getMerchantNo());
        credential.setStoreId(channelMerchant.getStoreId());
        return credential;
    }
}
