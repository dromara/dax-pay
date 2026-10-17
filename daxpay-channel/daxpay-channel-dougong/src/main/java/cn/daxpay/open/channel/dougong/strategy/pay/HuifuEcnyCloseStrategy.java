package cn.daxpay.open.channel.dougong.strategy.pay;

import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.service.isv.DougongIsvConfigAssembler;
import cn.daxpay.open.channel.dougong.service.payment.DougongCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付数字人民币支付关闭策略
///
/// 复用斗拱关单服务: 汇付关单接口(scanpay/close)按原汇付流水号(org_hf_seq_id)关闭,
/// 与底层支付方式无关, 数币订单(D_NATIVE)同样适用。
///
/// 注意: 汇付仅提供关单接口, 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HuifuEcnyCloseStrategy extends AbsPayCloseStrategy {

    private final DougongCloseService dougongCloseService;
    private final DougongIsvConfigAssembler dougongIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HUIFU_ECNY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        DougongSdkCredential credential = dougongIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return dougongCloseService.close(trade, credential, useCancel, context.getClientIp());
    }
}
