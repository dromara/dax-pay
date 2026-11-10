package cn.daxpay.open.channel.mock.strategy.pay;

import cn.daxpay.open.channel.mock.service.MockPayService;
import cn.daxpay.open.channel.mock.service.isv.MockIsvPaymentSwitchService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxIsvAppPair;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道服务商支付策略
///
/// 模拟支付服务商产品(ProductEnum.MOCK_ISV)下发起支付的具体执行策略, 形态照抄直连壳
/// [MockPayStrategy] + 服务商支付闸门:
/// doBeforePay 校验全局唯一模拟服务商支付开关(payEnable=false 拒新支付, 退款/同步/回调不受影响),
/// 微信 JSAPI/小程序族方式再解析服务商双应用回填(对齐微信服务商范式),
/// 支付执行委托给共用的 [MockPayService](凭证由 Service 内按 product 二选一组装),
/// 解析出的 sp/sub 双应用经上下文缓存后随请求下传(对齐商业版 spAppId/subAppId 契约)。
///
/// open 版 [WxAppFacade#resolveIsvPair] 无 isvNo 入参, sp 应用按通道能力绑/产品级平台默认绑解析,
/// 解析不到由门面抛「未配置该能力对应的平台应用」; 本类不额外强制显式 channelAppId。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvPayStrategy extends AbsNormalPayStrategy {

    private final MockPayService mockPayService;
    private final MockIsvPaymentSwitchService mockIsvPaymentSwitchService;
    private final WxAppFacade wxAppFacade;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_ISV;
    }

    /// 支付前预处理: 先校验全局唯一模拟服务商支付开关(仅拦新支付, 不影响存量交易处理);
    /// 微信 JSAPI/小程序族方式解析服务商双应用(sp+sub)并回填 channelAppId
    /// (优先 sub_appid, 空则 sp_appid), 解析失败抛异常整单失败; 其余方式到此为止
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        // 商户号/通道商户号: 支付路由回填后恒有值; 上下文/支付单快照仅作兜底(勿假设 param 恒有值)
        PayTrade trade = context.getTrade();
        String mchNo = StrUtil.blankToDefault(payParam.getMchNo(),
                trade == null ? null : trade.getMchNo());
        String channelMchNo = StrUtil.blankToDefault(payParam.getChannelMchNo(),
                StrUtil.blankToDefault(context.getChannelMchNo(), trade == null ? null : trade.getChannelMchNo()));
        mockIsvPaymentSwitchService.assertPaymentEnabled();
        // 仅微信 JSAPI/小程序族需要 wxAppId, 其余方式零行为变化
        if (!MockPayService.isWxJsapiFamily(payParam.getMethod())) {
            return;
        }
        // 解析服务商双应用(platform(sp) 必填 + merchant(sub) 可选), 失败整单失败
        WxIsvAppPair pair = wxAppFacade.resolveIsvPair(mchNo, channelMchNo,
                payParam.getCapability(), payParam.getChannelAppId(), ProductEnum.MOCK_ISV.getCode());
        // 回填实际使用的通道应用: 优先 sub_appid(与 openId 绑定侧), 否则 sp_appid
        payParam.setChannelAppId(StrUtil.blankToDefault(
                pair.merchant() == null ? null : pair.merchant().wxAppId(),
                pair.platform().wxAppId()));
        // 双应用结果缓存到上下文, doPay 直接透传 sp/sub 给 mock 子应用(避免用回填后的 sub_appid 二次解析)
        context.setChannelConfig(pair);
    }

    /// 执行支付: 委托共用 [MockPayService], 携带 doBeforePay 缓存的服务商双应用(非微信 JSAPI 族为空)
    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        return mockPayService.pay(context.getTrade(), context.getPayParam(), context.getNormalOrder(),
                context.getChannelConfig(WxIsvAppPair.class));
    }
}
