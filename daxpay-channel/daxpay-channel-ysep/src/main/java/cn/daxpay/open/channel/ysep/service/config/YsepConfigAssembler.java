package cn.daxpay.open.channel.ysep.service.config;

import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.entity.YsepKeyConfig;
import cn.daxpay.open.channel.ysep.strategy.product.YsepPayProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 银盛通道凭证组装器
///
/// 从通道密钥配置([YsepKeyConfig], 按通道商户号定位)组装通道调用凭证 [YsepSdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起银盛 API 调用。
///
/// 银盛一通道两产品(ysep_pay 银盛支付 / yseqt_pay 银盛e企通)共用本组装器与密钥表,
/// 通道商户号全局唯一, 行内 product 字段表达密钥行的产品归属;
/// 两产品凭证字段结构一致(businessCode 仅 ysep_pay 使用), 组装逻辑无产品分支。
/// 银盛无沙箱网关, 凭证不携带沙箱标记。
///
/// 供支付策略([cn.daxpay.open.channel.ysep.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepConfigAssembler {

    private final YsepKeyConfigService ysepKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final YsepPayProductStrategy productStrategy;

    /// 组装银盛通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(银盛不按能力路由, 保留对齐签名)
    /// @return 银盛 SDK 凭证
    public YsepSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(发起方商户号 + 收款商户号 + 证书对, 关键字段缺失 fail-fast)
        YsepKeyConfig keyConfig = ysepKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        YsepSdkCredential credential = new YsepSdkCredential();
        credential.setSponsorMchNo(keyConfig.getSponsorMchNo());
        credential.setPayeeMchNo(keyConfig.getPayeeMchNo());
        credential.setBusinessCode(keyConfig.getBusinessCode());
        credential.setMerchantCertPfx(keyConfig.getMerchantCertPfx());
        credential.setMerchantCertPassword(keyConfig.getMerchantCertPassword());
        credential.setYsepayPublicCert(keyConfig.getYsepayPublicCert());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付随单上送 wxAppId)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([YsepPayProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(扫码/H5/支付宝/银联)不解析。
    /// appid 可选(银盛后台已绑定时可不传), 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// 两产品微信应用所需能力集合一致, 统一按 ysep_pay 产品级默认绑解析。
    ///
    /// @param payParam 支付参数(channelAppId 回填后经子应用装 wxAppId 上送)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/H5/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.YSEP_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
