package cn.daxpay.open.plugin.easypay.param.order;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易支付协议退款订单查询参数(管理)
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付退款订单查询参数")
public class EasyPayRefundOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 平台退款单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台退款单号")
    private String refundNo;

    /// 商户退款单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户退款单号")
    private String bizRefundNo;

    /// 商户订单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /// 平台业务单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台业务单号")
    private String tradeNo;

    /// 易支付商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 协议退款状态 0=失败/处理中 1=成功
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "协议退款状态")
    private Integer status;

    /// API 版本 v1/v2
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "API版本")
    private String apiVersion;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
