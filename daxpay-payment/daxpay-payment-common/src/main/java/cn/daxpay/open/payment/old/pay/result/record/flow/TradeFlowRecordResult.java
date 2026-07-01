package cn.daxpay.open.payment.old.pay.result.record.flow;

import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 资金流水记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "资金流水记录")
public class TradeFlowRecordResult extends MchTradeBaseResult {

    /// 订单标题
    private String title;

    /// 金额（分，最小货币单位）
    private Long amount;

    /// 业务类型
    /// @see TradeFlowTypeEnum
    private String type;

    /// 支付产品
    /// @see ChannelEnum
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    private String channel;

    /// 平台交易号
    private String tradeNo;

    /// 商户交易号
    private String bizTradeNo;

    /// 通道交易号
    private String outTradeNo;
}

