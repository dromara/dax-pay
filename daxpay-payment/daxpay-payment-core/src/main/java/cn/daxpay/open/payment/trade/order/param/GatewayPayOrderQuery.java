package cn.daxpay.open.payment.trade.order.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关支付业务单查询参数(管理)
///
/// 查询维度与 [NormalPayOrderQuery] 对齐, 另含网关类型 gatewayType / 平台单号 orderNo。
@Data
@Accessors(chain = true)
@Schema(title = "网关支付业务单查询参数")
public class GatewayPayOrderQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台网关单号")
    private String orderNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "订单标题")
    private String title;

    /// @see cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "网关类型")
    private String gatewayType;

    /// @see cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务状态")
    private String status;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付能力")
    private String capability;

    /// 门店号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "门店号")
    private String storeNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;

    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "amount")
    @Schema(description = "金额下限(分)")
    private Long amountMin;

    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "amount")
    @Schema(description = "金额上限(分)")
    private Long amountMax;
}
