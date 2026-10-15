package cn.daxpay.open.channel.fuyou.service.direct;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.channel.fuyou.strategy.product.FuyouDirectProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友直连通道凭证组装器
///
/// 从直连商户密钥配置([FuyouDirectKeyConfig]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起富友「商户移动支付前置接口」(aggregatePay) 调用。
///
/// 沙箱标识直接读密钥配置固化的 [FuyouDirectKeyConfig#isSandbox]
/// (创建通道商户时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择 aipay 测试/生产网关。
///
/// 字段映射(对齐富友聚合接口):
/// - merchantNo ← mchntCd(富友商户号)
/// - mchntKey   ← mchntKey(商户密钥, MD5 签名)
/// - termNo     ← termNo(终端号)
/// - sandbox    ← 密钥配置固化标识
///
/// 供支付策略([cn.daxpay.open.channel.fuyou.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectConfigAssembler {

    private final FuyouDirectKeyConfigService fuyouDirectKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final FuyouDirectProductStrategy productStrategy;

    /// 组装富友直连通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 凭证按通道商户维度取)
    /// @param channelMchNo 通道商户号(直连密钥配置主键)
    /// @param capability   支付能力编码(保留参数, 富友不按能力路由)
    /// @return 富友直连 SDK 凭证(商户号 + 商户密钥 + 终端号)
    public FuyouDirectSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 直连商户密钥配置(取富友商户号 + 商户密钥 + 终端号 + sandbox; 缺失或关键字段为空时 fail-fast)
        FuyouDirectKeyConfig keyConfig = fuyouDirectKeyConfigService.getByChannelMchNoForPay(channelMchNo);

        FuyouDirectSdkCredential credential = new FuyouDirectSdkCredential();
        credential.setMerchantNo(keyConfig.getMchntCd());
        credential.setMchntKey(keyConfig.getMchntKey());
        credential.setTermNo(keyConfig.getTermNo());
        credential.setSandbox(keyConfig.isSandbox());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付随单上送 sub_appid)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([FuyouDirectProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(扫码/付款码/支付宝/银联)不解析。
    /// 富友 sub_appid 可选(富友后台已绑定时可不传), 未命中不回填不阻断。
    ///
    /// @param payParam 支付参数(channelAppId 回填后供子应用上送与订单落库)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/付款码/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.FUYOU_DIRECT.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
