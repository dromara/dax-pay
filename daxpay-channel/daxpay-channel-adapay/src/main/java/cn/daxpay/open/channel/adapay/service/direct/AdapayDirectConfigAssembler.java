package cn.daxpay.open.channel.adapay.service.direct;

import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig;
import cn.daxpay.open.channel.adapay.strategy.product.AdapayDirectProductStrategy;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
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
/// 从直连配置([AdapayDirectKeyConfig])读取 Adapay 应用 ID 与签名密钥
/// (apiKey/privateKey/publicKey), 组装为下发给子应用的通道调用凭证 [AdapaySdkCredential]。
///
/// Adapay 为聚合支付, 不区分应用能力(capability 参数保留但不使用);
/// 微信 JSAPI/小程序支付可上送 wx_app_id, 通过 [WxAppFacade#resolveOptional] 尽力解析
/// (商户档优先, 平台档兜底); Adapay 聚合通道 wxAppId 可选(汇付后台已绑定时可不传),
/// 未绑定微信应用时凭证 wxAppId 留空, 由通道层决定是否上送。
/// 需要解析的能力范围由产品策略
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
        // 1. 校验通用通道商户主表存在
        channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 直连配置(Adapay 应用ID + 签名密钥)
        AdapayDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 3. 组装凭证
        var credential = new AdapaySdkCredential();
        credential.setAdapayAppId(keyConfig.getAdapayAppId());
        credential.setApiKey(keyConfig.getApiKey());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());

        // 4. 产品策略声明需要绑定微信应用的能力(JSAPI/小程序)尽力解析 wx_app_id(Adapay 聚合通道 wxAppId 可选)
        //    (sync/close/refund 场景 capability 为空, 跳过解析不影响凭证复用)
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(capability);
        if (cap != null && productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            // Adapay 为聚合通道, wxAppId 可选(汇付后台已绑定时可不传), 尽力解析而非强制
            wxAppFacade.resolveOptional(mchNo, channelMchNo, capability, null, ProductEnum.ADA_PAY.getCode())
                    .ifPresent(app -> credential.setWxAppId(app.wxAppId()));
        }
        return credential;
    }
}
