package cn.daxpay.open.channel.mock.strategy.pay;

import cn.daxpay.open.channel.mock.service.MockPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 模拟通道支付策略
///
/// 模拟支付产品(ProductEnum.MOCK_PAY)下发起支付的具体执行策略。
/// mock 零资料接入, 无通道凭证可组装(doBeforePay 仅对微信 JSAPI/小程序方式解析回填微信应用,
/// 对齐微信直连范式, 其余方式空实现), 支付执行委托给 [MockPayService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockPayStrategy extends AbsNormalPayStrategy {

    private final MockPayService mockPayService;
    private final WxAppFacade wxAppFacade;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.MOCK_PAY;
    }

    /// 支付前预处理: 微信 JSAPI/小程序族方式解析微信应用并回填 channelAppId(对齐微信直连范式:
    /// 显式 channelAppId(必须已登记, 收银台 appid 例外) → 通道能力绑(仅商户档)), 解析失败抛异常整单失败;
    /// 其余方式 mock 零资料接入, 无通道凭证可组装, 保持空实现不解析(回落容器应用号快照)
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        // 仅微信 JSAPI/小程序族需要 wxAppId, 其余方式零行为变化
        if (!MockPayService.isWxJsapiFamily(payParam.getMethod())) {
            return;
        }
        // 商户号/通道商户号: open 的 NormalPayParam 经 MerchantPaymentCommonParam 继承二者,
        // 支付路由回填后恒有值; 上下文/支付单快照仅作兜底(勿假设 param 恒有值)
        PayTrade trade = context.getTrade();
        String mchNo = StrUtil.blankToDefault(payParam.getMchNo(),
                trade == null ? null : trade.getMchNo());
        String channelMchNo = StrUtil.blankToDefault(payParam.getChannelMchNo(),
                StrUtil.blankToDefault(context.getChannelMchNo(), trade == null ? null : trade.getChannelMchNo()));
        // 解析微信应用并回填实际使用的通道应用 AppId, 供 createOrder 落库(解析失败整单失败)
        WxAppView app = wxAppFacade.resolve(mchNo, channelMchNo,
                payParam.getCapability(), payParam.getChannelAppId(), ProductEnum.MOCK_PAY.getCode());
        payParam.setChannelAppId(app.wxAppId());
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        return mockPayService.pay(context.getTrade(), context.getPayParam(), context.getNormalOrder());
    }
}
