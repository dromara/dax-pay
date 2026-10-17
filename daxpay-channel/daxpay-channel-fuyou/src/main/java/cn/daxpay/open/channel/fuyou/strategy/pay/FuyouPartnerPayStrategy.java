package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友合作方支付策略
///
/// 富友合作方(ProductEnum.FUYOU_PARTNER)下发起支付的具体执行策略。
/// 合作方与服务商([FuyouPayStrategy])共用 spay 扫码标准接口族与请求构建, 仅两处差异:
/// - 凭证按 product=fuyou_partner 密钥行组装(委托 [FuyouIsvConfigAssembler] 产品参数重载)
/// - 异步通知走 `/fuyou/partner/pay` 路径段(回调侧按合作方密钥行验签)
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPartnerPayStrategy extends AbsNormalPayStrategy {

    private final FuyouPayService fuyouPayService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PARTNER;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装合作方通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(), ProductEnum.FUYOU_PARTNER);
        context.setChannelConfig(credential);
        // 微信 JSAPI/小程序解析微信应用(sub_appid)回填 channelAppId, 供子应用上送与订单落库; 未命中不阻断
        fuyouIsvConfigAssembler.resolveWxAppIfRequired(payParam, ProductEnum.FUYOU_PARTNER);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        FuyouSdkCredential credential = context.getChannelConfig(FuyouSdkCredential.class);
        // 回调路径段 partner/pay: 回调侧按合作方产品密钥行验签
        return fuyouPayService.pay(context.getTrade(), context.getPayParam(), credential, "partner/pay");
    }
}
