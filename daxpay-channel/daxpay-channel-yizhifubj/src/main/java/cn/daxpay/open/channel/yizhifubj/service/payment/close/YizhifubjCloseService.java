package cn.daxpay.open.channel.yizhifubj.service.payment.close;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付支付关闭业务服务
///
/// 一直付无主动关单接口(对齐参考实现能力: 订单依赖通道侧超时自动关闭),
/// 关单为本地语义: 平台侧置为关闭阻断后续支付路由, 通道侧二维码到时效自然失效。
/// 未支付订单即使被扫, 资金也会因订单超时被一直付退回, 不存在资损风险。
@Slf4j
@Service
public class YizhifubjCloseService {

    /// 关闭订单(本地关单, 不调用通道)
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        log.info("一直付本地关单(通道无主动关单接口): tradeNo={}, useCancel={}", trade.getTradeNo(), useCancel);
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
