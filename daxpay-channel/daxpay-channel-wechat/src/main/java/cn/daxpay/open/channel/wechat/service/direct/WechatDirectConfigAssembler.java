package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.channel.wechat.strategy.direct.pay.WechatDirectPayStrategy;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 微信直连通道凭证组装器
///
/// 从通道商户扩展([WechatDirectChannelMerchant]) + 密钥([WechatDirectKeyConfig])
/// + 主数据应用([WxAppFacade#resolve]) 组装为下发给子应用的通道调用凭证 [WechatSdkCredential]。
///
/// 应用解析由 facade 统一完成(显式 channelAppId → 通道能力绑 → 平台默认),
/// 直连场景期望解析到商户档([cn.daxpay.open.payment.auth.core.AppScopeEnum#MERCHANT])。
/// 密钥配置按通道商户号维度查询(一个商户号共享一套密钥/证书)。
///
/// 供支付策略([WechatDirectPayStrategy])组装通道调用凭证。
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
        // 下单路径已认证(initMch/MchContextLocalFilter), mchNo 归属由认证+拦截器保证, 无需显式断言
        WechatDirectChannelMerchant channelMerchant = this.loadChannelMerchant(channelMchNo);
        return this.assemble(app.wxAppId(), channelMchNo, channelMerchant);
    }

    /// 回调专用凭证组装(不解析应用)
    ///
    /// 回调验签+解密仅需 apiKeyV3 与平台证书, 不依赖 wxAppId(子应用 buildCallbackService 不校验 appId)。
    /// 故不查应用主数据, 凭证 appId 留空, 只装载密钥与商户号。
    /// 回调为 @IgnoreAuth, mchNo 来自 URL, 故显式校验通道商户归属(拦截器已 fail-closed, 此为防御性双保险)。
    public WechatSdkCredential buildCallbackConfig(String mchNo, String channelMchNo) {
        WechatDirectChannelMerchant channelMerchant = this.loadChannelMerchant(channelMchNo);
        this.assertOwnsMchNo(channelMerchant, mchNo);
        return this.assemble(null, channelMchNo, channelMerchant);
    }

    /// 转账专用凭证组装(按发起应用引用解析, 不经 capability)
    ///
    /// 转账场景的应用由「微信转账配置」显式指定(公众号), 不走支付能力绑定解析。
    /// 直接按 [transferAppRefId] 加载商户档应用取 wxAppId, 装载密钥。
    ///
    /// @param channelMchNo    通道商户号(密钥查询)
    /// @param transferAppRefId 转账发起应用引用(wx_mch_app 主键)
    /// @return 微信 SDK 凭证, wxAppId 来自转账配置指定的公众号应用
    public WechatSdkCredential buildTransferConfig(String channelMchNo, Long transferAppRefId) {
        WxAppView app = wxAppFacade.getById(AppScopeEnum.MERCHANT, transferAppRefId);
        if (app == null) {
            // 微信: 转账发起应用未配置或已删除
            throw new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                    "error.channel.wechat.transferAppNotConfigured");
        }
        WechatDirectChannelMerchant channelMerchant = this.loadChannelMerchant(channelMchNo);
        return this.assemble(app.wxAppId(), channelMchNo, channelMerchant);
    }

    /// 分账接收方绑定专用凭证组装(不经 capability, 显式指定应用)
    ///
    /// 接收方绑定无支付能力维度, 微信 V3 receivers/add 要求 appid,
    /// 且 openid 类型账号为 appid 维度, 故由绑定记录显式指定商户档应用 appid。
    ///
    /// @param mchNo        商户号(商户档应用隔离条件)
    /// @param channelMchNo 通道商户号(密钥与商户绑定定位)
    /// @param channelAppId 绑定所用商户档微信应用 appid
    /// @return 微信 SDK 凭证, wxAppId 来自显式指定的商户档应用
    public WechatSdkCredential buildAllocReceiverConfig(String mchNo, String channelMchNo, String channelAppId) {
        WxAppView app = wxAppFacade.resolve(mchNo, channelMchNo, null, channelAppId,
                ProductEnum.WECHAT_PAY.getCode());
        WechatDirectChannelMerchant channelMerchant = this.loadChannelMerchant(channelMchNo);
        return this.assemble(app.wxAppId(), channelMchNo, channelMerchant);
    }

    /// 加载通道商户绑定(channelMchNo 是系统生成号, 不等于 wxMchId)
    private WechatDirectChannelMerchant loadChannelMerchant(String channelMchNo) {
        return wechatDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 校验通道商户归属(回调无认证场景的防御性双保险)
    private void assertOwnsMchNo(WechatDirectChannelMerchant channelMerchant, String mchNo) {
        if (!Objects.equals(channelMerchant.getMchNo(), mchNo)) {
            // 微信: 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.channelMerchantMismatch");
        }
    }

    /// 组装通道调用凭证(wxAppId + 密钥 + 通道商户号)
    private WechatSdkCredential assemble(String wxAppId, String channelMchNo,
                                         WechatDirectChannelMerchant channelMerchant) {
        WechatDirectKeyConfig keyConfig = wechatDirectKeyConfigService.findByChannelMchNo(channelMchNo);

        WechatSdkCredential credential = new WechatSdkCredential();
        credential.setWxMchId(channelMerchant.getWxMchId());
        credential.setWxAppId(wxAppId);
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
}
