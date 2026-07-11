package cn.daxpay.open.payment.core.trade.mq;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关支付超时关单消息体
///
/// 预下单时按过期时间投递, 消费端只信任 orderNo 重新查库做幂等。
@Data
@Accessors(chain = true)
public class GatewayTimeoutMessage {

    /// 平台网关单号
    private String orderNo;

    /// 商户业务单号(日志追踪)
    private String bizOrderNo;
}
