package cn.daxpay.open.channel.sheng.strategy.refund;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.service.config.ShengIsvConfigAssembler;
import cn.daxpay.open.channel.sheng.service.payment.ShengRefundSyncService;
import cn.daxpay.open.payment.strategy.refund.AbsSyncRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通服务商(ISV)退款同步策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [ShengIsvConfigAssembler]: 服务商密钥全局一份 + 绑定行子商户身份),
/// 同步执行委托给 [ShengRefundSyncService](两模式共用, 聚合 API 同网关同端点同签名)。
///
/// 注意: getProduct 必须返回 SHENG_ISV(策略工厂对同产品码静默吞并, 误写会运行时走错凭证源)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvSyncRefundStrategy extends AbsSyncRefundStrategy {

    private final ShengRefundSyncService shengRefundSyncService;
    private final ShengIsvConfigAssembler shengIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.SHENG_ISV;
    }

    @Override
    public RefundResultBo doSync(RefundOrder refundOrder) {
        // 组装通道调用凭证
        ShengSdkCredential credential = shengIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return shengRefundSyncService.sync(refundOrder, credential);
    }
}
