package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连通道凭证组装器
///
/// 从进件对象([WechatDirectApp] + [WechatDirectKeyConfig])读取密钥/证书,
/// 组装为下发给子应用的通道调用凭证 [WechatSdkCredential]。
///
/// 应用解析优先级:
/// 1. channelAppId 显式指定(须预配) >
/// 2. 能力关联(显式配置 > appType 自动推导) >
/// 3. 通道商户首个应用。
/// 密钥配置按通道商户号维度查询(一个商户号共享一套密钥/证书)。
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.direct.WechatDirectPayStrategy])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectConfigAssembler {

    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WechatDirectKeyConfigService wechatDirectKeyConfigService;
    private final WechatDirectAppCapabilityService wechatDirectAppCapabilityService;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo         商户号(兜底定位应用)
    /// @param channelMchNo  通道商户号(系统生成号, 密钥查询与应用定位主键, 不等于微信商户号)
    /// @param capability    支付能力编码(用于选择匹配的应用)
    /// @param channelAppId  通道应用 AppId(可选; 非空则强制使用并校验预配)
    /// @return 微信 SDK 凭证, 字段对齐子应用 WechatSdkCredential
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability, String channelAppId) {
        WechatDirectApp app = resolveApp(channelMchNo, capability, channelAppId);
        WechatDirectKeyConfig keyConfig = wechatDirectKeyConfigService.findByChannelMchNo(channelMchNo);

        // 从通道商户绑定表取真实微信商户号(channelMchNo 是系统生成号, 不等于 wxMchId)
        WechatDirectChannelMerchant channelMerchant = wechatDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        WechatSdkCredential credential = new WechatSdkCredential();
        credential.setWxMchId(channelMerchant.getWxMchId());
        credential.setWxAppId(app.getWxAppId());
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

    /// 解析支付使用的应用
    ///
    /// 优先级: channelAppId 预配校验 > 能力解析 > 通道商户首个应用
    private WechatDirectApp resolveApp(String channelMchNo, String capability, String channelAppId) {
        if (StrUtil.isNotBlank(channelAppId)) {
            return wechatDirectAppManager.findByChannelMchNoAndWxAppId(channelMchNo, channelAppId)
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.channel.wechat.channelAppIdNotFound", channelAppId));
        }
        return wechatDirectAppCapabilityService.resolveApp(channelMchNo, capability)
                // 兜底: 按通道商户号取首个应用
                .or(() -> wechatDirectAppManager.findFirstByChannelMchNo(channelMchNo))
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
    }
}
