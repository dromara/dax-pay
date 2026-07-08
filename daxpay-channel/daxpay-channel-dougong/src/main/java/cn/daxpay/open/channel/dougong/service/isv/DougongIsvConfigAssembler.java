package cn.daxpay.open.channel.dougong.service.isv;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvChannelMerchantManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商通道凭证组装器
///
/// 从服务商密钥配置([DougongIsvKeyConfig]) + 通道商户绑定([DougongIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起斗拱(汇付) API 调用。
///
/// 字段映射:
/// - sysId/productId/privateKey/dgPublicKey ← [DougongIsvKeyConfig](服务商级, 全局唯一)
/// - merchantNo/appId ← [DougongIsvChannelMerchant](商户级)
///
/// 供支付策略([cn.daxpay.open.channel.dougong.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongIsvConfigAssembler {

    private final DougongIsvChannelMerchantManager dougongIsvChannelMerchantManager;
    private final DougongIsvKeyConfigService dougongIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装斗拱通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(斗拱商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 斗拱不按能力路由)
    /// @return 斗拱 SDK 凭证
    public DougongSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 服务商密钥(全局唯一, 含 sysId/productId/密钥; 缺失或关键字段为空时 fail-fast)
        DougongIsvKeyConfig keyConfig = dougongIsvKeyConfigService.getByProductForPay(ProductEnum.DOUGONG_PAY.getCode());
        // 通道商户绑定(取 merchantNo + appId)
        DougongIsvChannelMerchant channelMerchant = dougongIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 斗拱: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("payment.error.channel.channelMerchantNotExist"));

        DougongSdkCredential credential = new DougongSdkCredential();
        // 服务商身份与密钥
        credential.setSysId(keyConfig.getSysId());
        credential.setProductId(keyConfig.getProductId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setDgPublicKey(keyConfig.getDgPublicKey());
        // 沙箱状态读取支付产品配置的生效环境
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.DOUGONG_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setMerchantNo(channelMerchant.getMerchantNo());
        credential.setAppId(channelMerchant.getAppId());
        return credential;
    }
}
