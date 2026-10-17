package cn.daxpay.open.channel.yizhifubj.strategy.sync;

import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.service.direct.YizhifubjDirectConfigAssembler;
import cn.daxpay.open.channel.yizhifubj.service.payment.sync.YizhifubjSyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付直连支付同步策略
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjDirectSyncStrategy extends AbsSyncPayOrderStrategy {

    private final YizhifubjSyncService yizhifubjSyncService;
    private final YizhifubjDirectConfigAssembler yizhifubjDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.YIZHIFU_PAY;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        YizhifubjSdkCredential credential = yizhifubjDirectConfigAssembler.buildConfig(
                context.getTrade().getMchNo(), context.getChannelMchNo(), context.getCapability());
        return yizhifubjSyncService.sync(context.getTrade(), credential);
    }
}
