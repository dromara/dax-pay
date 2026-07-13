package cn.daxpay.open.channel.fuyou.service.isv;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商通道凭证组装器
///
/// 从服务商密钥配置([FuyouIsvKeyConfig]) + 通道商户绑定([FuyouIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起富友 API 调用。
///
/// 字段映射(对齐富友开放平台接口):
/// - fyAppId/orderPrefix/privateKey/publicKey ← [FuyouIsvKeyConfig] (服务商级, 全局唯一)
/// - merchantNo/termNo ← [FuyouIsvChannelMerchant] (富友商户号 + 终端号, 子商户级)
/// - sandbox ← PayProductConfig.activeEnv (沙箱/生产)
///
/// 供支付策略([cn.daxpay.open.channel.fuyou.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvConfigAssembler {

    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;
    private final FuyouIsvKeyConfigService fuyouIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装富友通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(富友商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 富友不按能力路由)
    /// @return 富友 SDK 凭证(含服务商密钥 + 富友商户号 + 终端号)
    public FuyouSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 沙箱状态读取支付产品配置的生效环境
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.FUYOU_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        // 服务商密钥(按生效环境取对应环境密钥, 含机构号 + 私钥/公钥 + 订单前缀; 缺失或关键字段为空时 fail-fast)
        FuyouIsvKeyConfig keyConfig = fuyouIsvKeyConfigService.getByProductForPay(ProductEnum.FUYOU_PAY.getCode(), sandbox);
        // 通道商户绑定(取富友商户号 + 终端号)
        FuyouIsvChannelMerchant channelMerchant = fuyouIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 富友: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 环境一致性校验
        if (channelMerchant.isSandbox() != sandbox) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.envMismatch");
        }

        FuyouSdkCredential credential = new FuyouSdkCredential();
        // 服务商身份与密钥
        credential.setFyAppId(keyConfig.getFyAppId());
        credential.setOrderPrefix(keyConfig.getOrderPrefix());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        // 子商户身份(富友商户号 + 终端号)
        String fuyouMchNo = channelMerchant.getFuyouMchNo();
        if (StrUtil.isBlank(fuyouMchNo)) {
            // 富友: 富友商户号未配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.fuyou.mchNoNotConfigured");
        }
        credential.setMerchantNo(fuyouMchNo);
        credential.setTermNo(channelMerchant.getTermNo());
        return credential;
    }
}
