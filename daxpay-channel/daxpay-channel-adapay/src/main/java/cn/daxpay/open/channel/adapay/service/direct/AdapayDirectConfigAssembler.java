package cn.daxpay.open.channel.adapay.service.direct;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.channel.adapay.strategy.product.AdapayDirectProductStrategy;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Adapay 直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product 与固化 sandbox 标识,
/// 从直连配置([AdapayDirectKeyConfig])读取 Adapay 应用 ID 与签名密钥
/// (apiKey/privateKey/publicKey), 组装为下发给子应用的通道调用凭证 [AdapaySdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// Adapay 为聚合支付, 不区分应用能力(capability 参数保留但不使用);
/// 微信 JSAPI/小程序支付需上送 wx_app_id, 通过 [WxAppFacade] 解析
/// (商户档优先, 平台档兜底), 需要解析的能力范围由产品策略
/// [AdapayDirectProductStrategy#wxAppRequiredCapabilities] 声明。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayDirectConfigAssembler {

    private final AdapayDirectKeyConfigService keyConfigService;
    private final ChannelMerchantManager channelMerchantManager;
    private final WxAppFacade wxAppFacade;
    private final AdapayDirectProductStrategy productStrategy;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(产品策略声明需要绑定微信应用的能力时解析微信应用, 其余能力不使用)
    /// @return Adapay SDK 凭证
    public AdapaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(Adapay 应用ID + 签名密钥, 按 sandbox 取对应环境密钥)
        AdapayDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);

        // 4. 组装凭证
        var credential = new AdapaySdkCredential();
        credential.setAdapayAppId(keyConfig.getAdapayAppId());
        credential.setApiKey(keyConfig.getApiKey());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);

        // 5. 产品策略声明需要绑定微信应用的能力(JSAPI/小程序)需上送 wx_app_id, 解析商户/平台级微信应用
        //    (sync/close/refund 场景 capability 为空, 跳过解析不影响凭证复用)
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(capability);
        if (cap != null && productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            WxAppView app = wxAppFacade.resolve(mchNo, channelMchNo, capability, null, ProductEnum.ADA_PAY.getCode());
            credential.setWxAppId(app.wxAppId());
        }
        return credential;
    }
}
