package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvKeyConfig;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 微信服务商通道凭证组装器
///
/// 从服务商密钥配置([WechatIsvKeyConfig]) + 服务商应用([WechatIsvApp]) +
/// 特约商户绑定([WechatIsvChannelMerchant]) + 子商户应用([WechatIsvMchApp]) 组装通道调用凭证,
/// 下发给子应用构建 WxJava 服务商模式 [com.github.binarywang.wxpay.service.WxPayService]。
///
/// 字段映射(对齐微信支付 V3 服务商接口):
/// - sp_mchid ← [WechatIsvKeyConfig#wxMchId] (服务商商户号, 全局唯一)
/// - sp_appid ← [WechatIsvApp#wxAppId] (服务商应用AppId, 按能力解析或 channelAppId 命中 SP)
/// - sub_mchid ← [WechatIsvChannelMerchant#subMchId] (特约商户号)
/// - sub_appid ← [WechatIsvMchApp#wxAppId] (子商户应用AppId, 可选; 未配置留空)
///
/// channelAppId 解析:
/// - 命中子商户应用 → 作为 sub_appid, sp 仍按 capability 解析
/// - 命中服务商应用 → 作为 sp_appid, sub 仍按 capability 可选解析
/// - 均未命中 → channelAppIdNotFound
///
/// 供支付策略([cn.daxpay.open.channel.wechat.strategy.isv.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvConfigAssembler {

    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;
    private final WechatIsvKeyConfigService wechatIsvKeyConfigService;
    private final WechatIsvAppCapabilityService wechatIsvAppCapabilityService;
    private final WechatIsvMchAppCapabilityService wechatIsvMchAppCapabilityService;
    private final WechatIsvAppManager wechatIsvAppManager;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;

    /// 组装服务商模式的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo         商户号(保留参数对齐直连签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo  通道商户号(特约商户绑定主键)
    /// @param capability    支付能力编码(用于解析应用)
    /// @param channelAppId  通道应用 AppId(可选; 非空则校验预配并优先)
    /// @return 微信 SDK 凭证(服务商模式, subMchId/subAppId 已填充)
    public WechatSdkCredential buildConfig(String mchNo, String channelMchNo, String capability, String channelAppId) {
        // 服务商密钥(全局唯一, 含 sp_mchid 与证书; 缺失或关键字段为空时 fail-fast)
        WechatIsvKeyConfig keyConfig = wechatIsvKeyConfigService.getByProductForPay(ProductEnum.WECHAT_ISV.getCode());
        // 特约商户绑定(取 sub_mchid)
        // 支付/回调须已装载 mchNo，通道商户走租户内查询
        WechatIsvChannelMerchant channelMerchant = wechatIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        WechatIsvApp isvApp;
        Optional<WechatIsvMchApp> mchApp;

        if (StrUtil.isNotBlank(channelAppId)) {
            // 优先匹配子商户应用(与 openId 绑定侧一致)
            Optional<WechatIsvMchApp> bySub = wechatIsvMchAppManager
                    .findByChannelMchNoAndWxAppIdNotTenant(channelMchNo, channelAppId);
            if (bySub.isPresent()) {
                mchApp = bySub;
                isvApp = wechatIsvAppCapabilityService.resolveApp(capability)
                        .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
            } else {
                // 再匹配服务商应用
                isvApp = wechatIsvAppManager.findByWxAppId(channelAppId)
                        .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                                "error.channel.wechat.channelAppIdNotFound", channelAppId));
                mchApp = wechatIsvMchAppCapabilityService.resolveApp(channelMchNo, capability);
            }
        } else {
            // 服务商应用为平台级(非 MchBaseEntity)，无租户过滤
            isvApp = wechatIsvAppCapabilityService.resolveApp(capability)
                    // 微信: 服务商应用不存在
                    .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
            // 子商户应用(取 sub_appid, 可选; 未配置时留空, SDK 仅用 sp_appid + sub_mchid 走服务商模式)
            mchApp = wechatIsvMchAppCapabilityService.resolveApp(channelMchNo, capability);
        }

        WechatSdkCredential credential = new WechatSdkCredential();
        // 服务商身份(sp_mchid / sp_appid)
        credential.setWxMchId(keyConfig.getWxMchId());
        credential.setWxAppId(isvApp.getWxAppId());
        // 特约商户身份(sub_mchid / sub_appid)
        credential.setSubMchId(channelMerchant.getSubMchId());
        credential.setSubAppId(mchApp.map(WechatIsvMchApp::getWxAppId).orElse(null));
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
