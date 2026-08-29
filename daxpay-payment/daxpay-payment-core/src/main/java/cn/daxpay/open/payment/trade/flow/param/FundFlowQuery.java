package cn.daxpay.open.payment.trade.flow.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 资金流水查询
///
@Data
@Accessors(chain = true)
@Schema(title = "资金流水查询")
public class FundFlowQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// @see cn.daxpay.open.payment.trade.flow.enums.FundFlowTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "流水类型(pay-收款/refund-退款)")
    private String flowType;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "原支付交易号")
    private String tradeNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "退款单号")
    private String refundNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付渠道")
    private String provider;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道交易号")
    private String outOrderNo;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
