package cn.daxpay.open.payment.trade.order.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款订单查询参数(管理)
///
@Data
@Accessors(chain = true)
@Schema(title = "退款订单查询参数")
public class RefundOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 退款号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "退款号")
    private String refundNo;

    /// 商户退款号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /// 原支付资金交易号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 交易类型(原支付形态)
    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易类型")
    private String tradeType;

    /// 商户业务订单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 退款状态
    /// @see cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "退款状态")
    private String status;

    /// 支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 门店号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "门店号")
    private String storeNo;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
