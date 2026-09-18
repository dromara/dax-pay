package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.channel.sheng.strategy.product.ShengPayProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 盛付通通道凭证组装器
///
/// 从通道密钥配置([ShengKeyConfig], 按通道商户号定位)组装通道调用凭证 [ShengSdkCredential],
/// 下发给子应用 dax-pay-channel-two 发起盛付通 API 调用。
///
/// 盛付通分商户(sheng_pay)/服务商(sheng_isv)双产品, 本组装器仅服务商户模式(服务商凭证见 [ShengIsvConfigAssembler]);
/// 无沙箱双环境(官方不提供集测接口), 凭证不携带沙箱标记。
/// 盛付通为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.sheng.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengConfigAssembler {

    private final ShengKeyConfigService shengKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final ShengPayProductStrategy productStrategy;

    /// 组装盛付通通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥按通道商户号定位)
    /// @param channelMchNo 通道商户号(定位密钥配置)
    /// @param capability   支付能力编码(盛付通不按能力路由, 保留对齐签名)
    /// @return 盛付通 SDK 凭证
    public ShengSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 密钥配置(商户身份 mchId/sdpAppId + 签名密钥对, 关键字段缺失 fail-fast)
        ShengKeyConfig keyConfig = shengKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        // 组装凭证
        ShengSdkCredential credential = new ShengSdkCredential();
        credential.setMchId(keyConfig.getShengMchId());
        credential.setSdpAppId(keyConfig.getSdpAppId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setShengpayPublicKey(keyConfig.getShengpayPublicKey());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付随单上送 extra.appId)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([ShengPayProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(扫码/H5/APP/付款码/支付宝/银联)不解析。
    /// appid 可选(盛付通后台已绑定时可不传), 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// @param payParam 支付参数(channelAppId 回填后经子应用装 extra.appId 上送)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/H5/APP/付款码/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.SHENG_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
