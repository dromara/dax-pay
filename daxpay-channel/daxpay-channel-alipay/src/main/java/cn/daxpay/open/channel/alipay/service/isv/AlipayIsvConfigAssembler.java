package cn.daxpay.open.channel.alipay.service.isv;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商通道凭证组装器
///
/// 从服务商应用([AlipayIsvApp] + [AlipayIsvAppKeyConfig])与子商户授权绑定([AlipayIsvChannelMerchant])
/// 组装下发给子应用的通道调用凭证 [AlipaySdkCredential]。
///
/// 服务商代调用模式: appId / 私钥 / 证书取自服务商应用, 应用授权令牌(appAuthToken)取自子商户授权绑定,
/// 子应用据此以服务商身份代子商户发起支付宝请求。
///
/// 供服务商支付/同步/关闭策略组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvConfigAssembler {

    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;
    private final AlipayIsvAppKeyConfigService alipayIsvAppKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装服务商模式的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo 平台商户号(定位子商户授权绑定)
    /// @return 支付宝 SDK 凭证, 字段对齐子应用 AlipaySdkCredential
    public AlipaySdkCredential buildConfig(String mchNo) {
        // 子商户授权绑定(含 appAuthToken)
        AlipayIsvChannelMerchant isvMerchant = alipayIsvChannelMerchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        // 服务商应用(appId 来源)
        AlipayIsvApp isvApp = alipayIsvAppManager.findById(isvMerchant.getIsvAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        // 服务商应用密钥/证书配置
        AlipayIsvAppKeyConfig keyConfig = alipayIsvAppKeyConfigService.findByAlipayIsvAppId(isvMerchant.getIsvAppId());

        AlipaySdkCredential credential = new AlipaySdkCredential();
        credential.setAliAppId(isvApp.getAliAppId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setAlipayPublicKey(keyConfig.getAlipayPublicKey());
        credential.setAuthType(keyConfig.getAuthType());
        credential.setAppCert(keyConfig.getAppCert());
        credential.setAlipayCert(keyConfig.getAlipayCert());
        credential.setAlipayRootCert(keyConfig.getAlipayRootCert());
        // 应用授权令牌(服务商代子商户调用接口的凭据)
        credential.setAppAuthToken(isvMerchant.getAppAuthToken());
        // 读取服务商支付产品当前生效环境, 判断是否沙箱
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.ALIPAY_ISV.getCode())
                .map(c -> PayEnvEnum.SANDBOX.getCode().equals(c.getActiveEnv()))
                .orElse(false);
        credential.setSandbox(sandbox);
        return credential;
    }
}
