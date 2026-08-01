package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvKeyConfig;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 微信服务商通道凭证组装器
///
/// 从服务商密钥配置([WechatIsvKeyConfig]) + 特约商户绑定([WechatIsvChannelMerchant])
/// + 主数据应用([WxAppFacade#resolveIsvPair]) 组装通道调用凭证,
/// 下发给子应用构建 WxJava 服务商模式。
///
/// 字段映射(对齐微信支付 V3 服务商接口):
/// - sp_mchid ← [WechatIsvKeyConfig#getWxMchId] (服务商商户号, 全局唯一)
/// - sp_appid ← pair.platform().wxAppId() (平台档应用, 按能力/channelAppId 解析)
/// - sub_mchid ← [WechatIsvChannelMerchant#getSubMchId] (特约商户号)
/// - sub_appid ← pair.merchant() optional (商户档应用, 未配置留空)
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.isv])组装通道调用凭证。
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
        String subAppId = pair.merchant() != null ? pair.merchant().wxAppId() : null;
        return this.assemble(pair.platform().wxAppId(), subAppId, keyConfig, channelMerchant);
    }

    /// 回调专用凭证组装(不解析应用)
    ///
    /// 回调验签+解密仅需 apiKeyV3 与平台证书, 不依赖 sp_appid/sub_appid。
    /// 故不查应用主数据, sp_appid/sub_appid 留空, 只装载密钥与特约商户号。
    /// 回调为 @IgnoreAuth, mchNo 来自 URL, 故显式校验通道商户归属(拦截器已 fail-closed, 此为防御性双保险)。
    public WechatSdkCredential buildCallbackConfig(String mchNo, String channelMchNo) {
        // 服务商密钥(全局唯一, 含 sp_mchid 与证书; 缺失或关键字段为空时 fail-fast)
        WechatIsvKeyConfig keyConfig = wechatIsvKeyConfigService.getByProductForPay(ProductEnum.WECHAT_ISV.getCode());
        // 特约商户绑定(取 sub_mchid)
        // 支付/回调须已装载 mchNo，通道商户走租户内查询
        WechatIsvChannelMerchant channelMerchant = wechatIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        this.assertOwnsMchNo(channelMerchant, mchNo);
        return this.assemble(null, null, keyConfig, channelMerchant);
    }

    /// 校验通道商户归属(回调无认证场景的防御性双保险)
    private void assertOwnsMchNo(WechatIsvChannelMerchant channelMerchant, String mchNo) {
        if (!Objects.equals(channelMerchant.getMchNo(), mchNo)) {
            // 微信: 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.channelMerchantMismatch");
        }
    }

    /// 组装通道调用凭证(sp_appid/sub_appid + 服务商密钥 + 特约商户绑定)
    private WechatSdkCredential assemble(String spAppId, String subAppId, WechatIsvKeyConfig keyConfig,
                                         WechatIsvChannelMerchant channelMerchant) {
        WechatSdkCredential credential = new WechatSdkCredential();
        // 服务商身份(sp_mchid / sp_appid)
        credential.setWxMchId(keyConfig.getWxMchId());
        credential.setWxAppId(spAppId);
        // 特约商户身份(sub_mchid / sub_appid)
        credential.setSubMchId(channelMerchant.getSubMchId());
        credential.setSubAppId(subAppId);
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
}
