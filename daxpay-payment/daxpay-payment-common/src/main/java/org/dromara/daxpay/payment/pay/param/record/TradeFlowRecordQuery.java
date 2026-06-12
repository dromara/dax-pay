package org.dromara.daxpay.payment.pay.param.record;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import org.dromara.daxpay.payment.pay.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 流水记录查询类
///
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "流水记录查询类")
public class TradeFlowRecordQuery extends MchQuery {

    /// 订单标题
    @Schema(description = "订单标题")
    private String title;

    /// 业务类型
    /// @see TradeFlowTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务类型")
    private String type;


    /// 支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 平台交易号
    @Schema(description = "平台交易号")
    private String tradeNo;

    /// 商户交易号
    @Schema(description = "商户交易号")
    private String bizTradeNo;

    /// 通道交易号
    @Schema(description = "通道交易号")
    private String outTradeNo;
}

