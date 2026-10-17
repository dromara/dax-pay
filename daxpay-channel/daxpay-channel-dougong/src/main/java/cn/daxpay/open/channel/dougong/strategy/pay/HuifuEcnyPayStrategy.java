package cn.daxpay.open.channel.dougong.strategy.pay;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongPayService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付数字人民币支付策略
///
/// 复用斗拱服务族: 数字人民币与聚合支付(DOUGONG_PAY)同通道同子应用接口(jspay),
/// 仅 tradeType 不同(D_NATIVE, 由支付方式映射决定), 支付执行与凭证组装完全同构。
///
/// 凭证复用斗拱服务商密钥(数币同一套斗拱密钥, 见 [DougongIsvConfigAssembler]);
/// 扫码模式无需用户/钱包标识, 不做微信应用解析。
@Slf4j
@Service
@RequiredArgsConstructor
public class HuifuEcnyPayStrategy extends AbsNormalPayStrategy {

    private final DougongPayService dougongPayService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HUIFU_ECNY;
    }

    /// 支付前预处理: 从容器读取通道路由参数, 组装通道凭证写入上下文
    @Override
    public void doBeforePay(PayStrategyContext context) {
        NormalPayParam payParam = context.getPayParam();
        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability());
        context.setChannelConfig(credential);
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        DougongSdkCredential credential = context.getChannelConfig(DougongSdkCredential.class);
        return dougongPayService.pay(context.getTrade(), context.getPayParam(), credential);
    }
}
