package cn.daxpay.open.payment.old.pay.param.record;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.payment.old.pay.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付回调信息记录
///
@EqualsAndHashCode(callSuper = true)
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "支付回调信息记录")
public class TradeCallbackRecordQuery extends MchQuery {

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;


    /// 支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 支付通道
    /// @see ChannelEnum#getCode()
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 回调类型
    /// @see TradeTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调类型")
    private String callbackType;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "回调处理状态")
    private String status;
}

