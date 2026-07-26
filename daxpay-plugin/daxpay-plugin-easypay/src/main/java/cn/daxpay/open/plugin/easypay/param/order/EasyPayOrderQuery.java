package cn.daxpay.open.plugin.easypay.param.order;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 易支付协议订单查询参数(管理)
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付订单查询参数")
public class EasyPayOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 商户订单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /// 平台业务单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台业务单号")
    private String tradeNo;

    /// 商品名称
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商品名称")
    private String name;

    /// 协议支付方式 alipay/wxpay/aggregate
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "协议支付方式")
    private String type;

    /// 协议状态 0=待付 1=成功
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "协议状态")
    private Integer status;

    /// 易支付商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "易支付商户号")
    private Integer pid;

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
