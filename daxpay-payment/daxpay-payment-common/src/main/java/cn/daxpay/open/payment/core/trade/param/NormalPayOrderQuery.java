package cn.daxpay.open.payment.core.trade.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 普通支付业务单查询参数(管理)
///
@Data
@Accessors(chain = true)
@Schema(title = "普通支付业务单查询参数")
public class NormalPayOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 商户业务单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 订单标题
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "订单标题")
    private String title;

    /// 业务状态
    /// @see cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务状态")
    private String status;

    /// 支付通道
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 支付方式
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付方式")
    private String method;

    /// 支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

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
