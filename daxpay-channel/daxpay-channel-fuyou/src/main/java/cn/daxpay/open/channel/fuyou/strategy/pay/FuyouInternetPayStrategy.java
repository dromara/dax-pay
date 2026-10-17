package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouInternetPayService;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaConfigAssembler;
import cn.daxpay.open.channel.fuyou.strategy.product.FuyouInternetProductStrategy;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友互联网直连支付策略
///
/// 富友互联网直连(ProductEnum.FUYOU_INTERNET)下发起支付的具体执行策略。
/// 配置组装在 [#doBeforePay] 阶段完成(委托 [FuyouRsaConfigAssembler]),
/// 支付执行委托给 [FuyouInternetPayService](hlwnets/aggpcpay, JSON + RSA 加密信封)。
///
/// 微信 JSAPI/小程序解析微信应用(appid)回填 channelAppId, 供子应用随单上送与订单落库; 未命中不阻断。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetPayStrategy extends AbsNormalPayStrategy {

    private final FuyouInternetPayService fuyouInternetPayService;
    private final FuyouRsaConfigAssembler fuyouRsaConfigAssembler;
    private final FuyouInternetProductStrategy productStrategy;
    private final WxAppFacade wxAppFacade;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_INTERNET;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        FuyouRsaSdkCredential credential = fuyouRsaConfigAssembler.buildConfig(payParam.getChannelMchNo());
        context.setChannelConfig(credential);
        // 微信 JSAPI/小程序解析微信应用(appid)回填 channelAppId(富友侧可选上送, 未命中不阻断)
        resolveWxAppIfRequired(payParam);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FuyouRsaSdkCredential credential = context.getChannelConfig(FuyouRsaSdkCredential.class);
        return fuyouInternetPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }

    /// 尽力解析微信应用并回填 channelAppId(仅 JSAPI/小程序能力解析)
    ///
    /// 解析顺序与 [WxAppFacade#resolveOptional] 一致; 富友 appid 可选(富友后台已绑定时可不传),
    /// 未命中不回填不阻断。
    private void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.FUYOU_INTERNET.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
