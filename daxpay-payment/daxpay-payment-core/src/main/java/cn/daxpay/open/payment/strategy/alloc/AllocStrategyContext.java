package cn.daxpay.open.payment.strategy.alloc;

import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 分账策略上下文
///
/// 请求级数据载体(函数传参), 与线程级身份上下文 [cn.daxpay.open.payment.common.context.PaymentContext] 严格区分。
/// 携带分账单(含通道凭证快照) + 明细列表, 供通道策略组装通道请求并回写结果。
@Data
@Accessors(chain = true)
public class AllocStrategyContext {

    /// 分账单(主表, 含通道凭证快照)
    private AllocOrder allocOrder;

    /// 分账明细列表(每个接收方一行)
    private List<AllocDetail> details;

    /// 通道编码(冗余, 便于策略使用)
    private String channel;

    /// 商户号
    private String mchNo;

    /// 通道商户号(凭证组装用)
    private String channelMchNo;

    /// 通道应用 AppId(凭证组装用)
    private String channelAppId;

    /// 原支付通道订单号(分账上送通道用)
    private String outOrderNo;

    /// 回调通知地址(部分通道需上送, 如抖音)
    private String notifyUrl;
}
