package cn.daxpay.open.payment.trade.record.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道入站回调记录查询
///
@Data
@Accessors(chain = true)
@Schema(title = "通道入站回调记录查询")
public class PayCallbackRecordQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台交易号")
    private String tradeNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道交易号")
    private String outTradeNo;

    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// @see TradeFlowTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调类型")
    private String callbackType;

    /// @see CallbackStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调处理状态")
    private String status;
}
