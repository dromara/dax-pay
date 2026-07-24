package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvKeyConfig;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商通道凭证组装器
///
/// 从服务商密钥配置([WechatIsvKeyConfig]) + 特约商户绑定([WechatIsvChannelMerchant])
/// + 主数据应用([WxAppFacade#resolveIsvPair]) 组装通道调用凭证,
/// 下发给子应用构建 WxJava 服务商模式 [com.github.binarywang.wxpay.service.WxPayService]。
///
/// 字段映射(对齐微信支付 V3 服务商接口):
/// - sp_mchid ← [WechatIsvKeyConfig#wxMchId] (服务商商户号, 全局唯一)
/// - sp_appid ← pair.platform().wxAppId() (平台档应用, 按能力/channelAppId 解析)
/// - sub_mchid ← [WechatIsvChannelMerchant#subMchId] (特约商户号)
/// - sub_appid ← pair.merchant() optional (商户档应用, 未配置留空)
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.isv.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvConfigAssembler {

    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WechatIsvKeyConfigService wechatIsvKeyConfigService;
    private final WxAppFacade wxAppFacade;

    /// 组装服务商模式的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo         商户号(用于主数据商户档应用定位)
    /// @param channelMchNo  通道商户号(特约商户绑定主键)
    /// @param capability    支付能力编码(用于解析应用)
    /// @param channelAppId  通道应用 AppId(可选; 非空则由 facade 校验预配并优先)
    /// @return 微信 SDK 凭证(服务商模式, subMchId/subAppId 已填充)
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability, String channelAppId) {
        // 服务商密钥(全局唯一, 含 sp_mchid 与证书; 缺失或关键字段为空时 fail-fast)
        WechatIsvKeyConfig keyConfig = wechatIsvKeyConfigService.getByProductForPay(ProductEnum.WECHAT_ISV.getCode());
        // 特约商户绑定(取 sub_mchid)
        // 支付/回调须已装载 mchNo，通道商户走租户内查询
        WechatIsvChannelMerchant channelMerchant = wechatIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 主数据: platform(sp) 必填 + merchant(sub) 可选（产品级平台默认绑）
        WxIsvAppPair pair = wxAppFacade.resolveIsvPair(mchNo, channelMchNo, capability, channelAppId,
                ProductEnum.WECHAT_ISV.getCode());

        WechatSdkCredential credential = new WechatSdkCredential();
        // 服务商身份(sp_mchid / sp_appid)
        credential.setWxMchId(keyConfig.getWxMchId());
        credential.setWxAppId(pair.platform().wxAppId());
        // 特约商户身份(sub_mchid / sub_appid)
        credential.setSubMchId(channelMerchant.getSubMchId());
        credential.setSubAppId(pair.merchant() != null ? pair.merchant().wxAppId() : null);
        // 服务商密钥与证书
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
