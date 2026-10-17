package cn.daxpay.open.channel.helipay.service.payment;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝支付关闭业务服务
///
/// 合利宝扫码产品线无主动关单接口(对齐参考实现能力: 订单依赖通道侧超时自动关闭),
/// 关单为本地语义: 平台侧置为关闭阻断后续支付路由, 通道侧二维码到时效自然失效。
/// 未支付订单即使被扫, 资金也会因订单超时被合利宝退回, 不存在资损风险。
@Slf4j
@Service
public class HelipayCloseService {

    /// 关闭订单(本地关单, 不调用通道)
    public CloseTypeEnum close(PayTrade trade, boolean useCancel) {
        log.info("合利宝本地关单(通道无主动关单接口): tradeNo={}, useCancel={}", trade.getTradeNo(), useCancel);
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
