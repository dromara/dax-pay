package cn.daxpay.open.payment.trade.order.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关支付业务单查询参数(管理)
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

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "网关类型")
    private String gatewayType;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务状态")
    private String status;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
