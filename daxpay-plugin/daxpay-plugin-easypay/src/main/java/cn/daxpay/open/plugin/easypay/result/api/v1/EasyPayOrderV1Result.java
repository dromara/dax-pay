package cn.daxpay.open.plugin.easypay.result.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付 V1 订单查询响应
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V1订单查询响应")
public class EasyPayOrderV1Result {

    /// 返回状态码（1 成功）
    @Schema(description = "返回状态码")
    private Integer code;

    /// 错误信息
    @Schema(description = "错误信息")
    private String msg;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 协议支付方式
    @Schema(description = "支付方式")
    private String type;

    /// 协议状态 0待付 1成功
    @Schema(description = "支付状态 0待付 1成功")
    private Integer status;

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 订单创建时间
    @Schema(description = "订单创建时间")
    private String addtime;

    /// 订单完成时间
    @Schema(description = "订单完成时间")
    private String endtime;

    /// 商品名称
    @Schema(description = "商品名称")
    private String name;

    /// 金额（元，字符串）
    @Schema(description = "金额（元）")
    private String money;

    /// 业务扩展参数
    @Schema(description = "业务扩展参数")
    private String param;

    /// 支付用户标识
    @Schema(description = "支付用户标识")
    private String buyer;
}
