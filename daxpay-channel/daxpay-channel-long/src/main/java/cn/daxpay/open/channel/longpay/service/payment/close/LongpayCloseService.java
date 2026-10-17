package cn.daxpay.open.channel.longpay.service.payment.close;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付支付关闭业务服务
///
/// 建行龙支付聚合线无公开可考证的主动关单接口, 关单为本地语义:
/// 平台侧置为关闭阻断后续支付路由, 通道侧聚合码依赖下单 TIMEOUT 到时自然失效。
/// 未支付订单即使被扫, 资金也会因订单超时由建行退回, 不存在资损风险。
@Slf4j
@Service
public class LongpayCloseService {

    /// 关闭订单(本地关单, 不调用通道)
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        log.info("建行龙支付本地关单(聚合码随 TIMEOUT 失效): tradeNo={}, useCancel={}", trade.getTradeNo(), useCancel);
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
