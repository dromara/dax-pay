package cn.daxpay.open.payment.trade.abnormal.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 异常订单查询
///
@Data
@Accessors(chain = true)
@Schema(title = "异常订单查询")
public class AbnormalOrderQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台交易号")
    private String tradeNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalOrderTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "异常类型")
    private String abnormalType;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalSourceEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "发现来源")
    private String source;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付渠道")
    private String provider;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道交易号")
    private String outOrderNo;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalHandleStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "处理状态")
    private String handleStatus;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
