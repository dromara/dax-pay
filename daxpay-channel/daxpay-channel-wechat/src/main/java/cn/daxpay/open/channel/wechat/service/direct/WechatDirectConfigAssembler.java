package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连通道凭证组装器
///
/// 从通道商户扩展([WechatDirectChannelMerchant]) + 密钥([WechatDirectKeyConfig])
/// + 主数据应用([WxAppFacade#resolve]) 组装为下发给子应用的通道调用凭证 [WechatSdkCredential]。
///
/// 应用解析由 facade 统一完成(显式 channelAppId → 通道能力绑 → 平台默认),
/// 直连场景期望解析到商户档([cn.daxpay.open.payment.wx.enums.WxAppScopeEnum#MERCHANT])。
/// 密钥配置按通道商户号维度查询(一个商户号共享一套密钥/证书)。
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.direct.WechatDirectPayStrategy])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectConfigAssembler {

    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WechatDirectKeyConfigService wechatDirectKeyConfigService;
    private final WxAppFacade wxAppFacade;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo         商户号(主数据商户档应用定位)
    /// @param channelMchNo  通道商户号(系统生成号, 密钥查询与应用定位主键, 不等于微信商户号)
    /// @param capability    支付能力编码(用于选择匹配的应用)
    /// @param channelAppId  通道应用 AppId(可选; 非空则强制使用并校验预配)
    /// @return 微信 SDK 凭证, 字段对齐子应用 WechatSdkCredential
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability, String channelAppId) {
        // 主数据应用(直连期望 merchant 档；产品级平台默认绑兜底)
        WxAppView app = wxAppFacade.resolve(mchNo, channelMchNo, capability, channelAppId,
                ProductEnum.WECHAT_PAY.getCode());
        WechatDirectKeyConfig keyConfig = wechatDirectKeyConfigService.findByChannelMchNo(channelMchNo);

        // 从通道商户绑定表取真实微信商户号(channelMchNo 是系统生成号, 不等于 wxMchId)
        WechatDirectChannelMerchant channelMerchant = wechatDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        WechatSdkCredential credential = new WechatSdkCredential();
        credential.setWxMchId(channelMerchant.getWxMchId());
        credential.setWxAppId(app.wxAppId());
        // 密钥与证书
        credential.setApiKeyV3(keyConfig.getApiKeyV3());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPrivateCert(keyConfig.getPrivateCert());
        credential.setCertSerialNo(keyConfig.getCertSerialNo());
        // 支付公钥新模式(为空则子应用走平台证书模式)
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setPublicKeyId(keyConfig.getPublicKeyId());
        return credential;
    }

    /// 兼容无 channelAppId 的调用(回调等)
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        return buildConfig(mchNo, channelMchNo, capability, null);
    }
}
