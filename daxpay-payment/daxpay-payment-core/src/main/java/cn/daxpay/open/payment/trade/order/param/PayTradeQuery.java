package cn.daxpay.open.payment.trade.order.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 资金交易凭证查询参数(管理)
///
@Data
@Accessors(chain = true)
@Schema(title = "资金交易凭证查询参数")
public class PayTradeQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 支付交易号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付交易号")
    private String tradeNo;

    /// 通道订单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道订单号")
    private String outOrderNo;

    /// 资金状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "资金状态")
    private String status;

    /// 交易形态
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易形态")
    private String tradeType;

    /// 支付通道
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    // 注意: pay_trade 表无 method/product 列，禁止在此加无效筛选

    /// 通道商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 门店号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "门店号")
    private String storeNo;

    /// 关联容器ID
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "关联容器ID")
    private Long containerId;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;

    /// 金额下限(最小货币单位)
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "amount")
    @Schema(description = "金额下限(分)")
    private Long amountMin;

    /// 金额上限(最小货币单位)
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "amount")
    @Schema(description = "金额上限(分)")
    private Long amountMax;
}
