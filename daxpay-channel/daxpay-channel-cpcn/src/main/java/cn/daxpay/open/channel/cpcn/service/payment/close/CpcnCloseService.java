package cn.daxpay.open.channel.cpcn.service.payment.close;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 中金支付关闭业务服务
///
/// 中金线无主动关单接口(对齐参考实现能力: 订单依赖 ExpirePeriod 到时自动关闭),
/// 关单为本地语义: 平台侧置为关闭阻断后续支付路由, 通道侧二维码到时效自然失效。
/// 未支付订单即使被扫, 资金也会因订单超时被中金退回, 不存在资损风险。
@Slf4j
@Service
public class CpcnCloseService {

    /// 关闭订单(本地关单, 不调用通道)
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        log.info("中金本地关单(通道无主动关单接口): tradeNo={}, useCancel={}", trade.getTradeNo(), useCancel);
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
