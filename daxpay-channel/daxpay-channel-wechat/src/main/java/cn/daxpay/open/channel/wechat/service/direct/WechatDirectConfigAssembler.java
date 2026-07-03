package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信直连通道凭证组装器
///
/// 从进件对象([WechatDirectApp] + [WechatDirectKeyConfig])读取密钥/证书,
/// 组装为下发给子应用的通道调用凭证 [WechatSdkCredential]。
///
/// 应用解析优先级(委托 [WechatDirectAppCapabilityService.resolveApp]):
/// 能力关联(显式配置 > appType自动推导) > 通道商户首个应用。
/// 密钥配置按通道商户号维度查询(一个商户号共享一套密钥/证书)。
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.direct.WechatDirectPayStrategy])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectConfigAssembler {

    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectKeyConfigService wechatDirectKeyConfigService;
    private final WechatDirectAppCapabilityService wechatDirectAppCapabilityService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(兜底定位应用)
    /// @param channelMchNo 通道商户号(微信商户号, 密钥查询与应用定位主键)
    /// @param capability   支付能力编码(用于选择匹配的应用)
    /// @return 微信 SDK 凭证, 字段对齐子应用 WechatSdkCredential
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        WechatDirectApp app = resolveApp(mchNo, channelMchNo, capability);
        WechatDirectKeyConfig keyConfig = wechatDirectKeyConfigService.findByChannelMchNo(channelMchNo);

        WechatSdkCredential credential = new WechatSdkCredential();
        // 微信商户号 = 通道商户号
        credential.setWxMchId(app.getChannelMchNo());
        credential.setWxAppId(app.getWxAppId());
        // 密钥与证书
        credential.setApiKeyV3(keyConfig.getApiKeyV3());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPrivateCert(keyConfig.getPrivateCert());
        credential.setCertSerialNo(keyConfig.getCertSerialNo());
        // 支付公钥新模式(为空则子应用走平台证书模式)
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setPublicKeyId(keyConfig.getPublicKeyId());
        // 读取支付产品当前生效环境, 判断是否沙箱
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.WECHAT_PAY.getCode())
                .map(c -> PayEnvEnum.SANDBOX.getCode().equals(c.getActiveEnv()))
                .orElse(false);
        credential.setSandbox(sandbox);
        return credential;
    }

    /// 解析支付使用的应用
    ///
    /// 优先级(委托 [WechatDirectAppCapabilityService.resolveApp]):
    /// 能力关联(显式配置 > appType自动推导) > 通道商户首个应用
    private WechatDirectApp resolveApp(String mchNo, String channelMchNo, String capability) {
        return wechatDirectAppCapabilityService.resolveApp(channelMchNo, capability)
                // 兜底: 按通道商户号取首个应用
                .or(() -> wechatDirectAppManager.findFirstByChannelMchNo(channelMchNo))
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
    }
}
