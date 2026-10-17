package cn.daxpay.open.channel.youzan.service.payment.close;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 有赞支付关闭业务服务
///
/// 有赞(高汇通)统收单线无主动关单接口(对齐参考实现能力: 订单依赖 expire_time 到期自动关闭),
/// 关单为本地语义: 平台侧置为关闭阻断后续支付路由, 通道侧订单到时效自然失效。
/// 未支付订单即使被唤起, 资金也会因订单过期被有赞退回, 不存在资损风险。
@Slf4j
@Service
public class YouzanCloseService {

    /// 关闭订单(本地关单, 不调用通道)
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        log.info("有赞本地关单(通道无主动关单接口): tradeNo={}, useCancel={}", trade.getTradeNo(), useCancel);
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
