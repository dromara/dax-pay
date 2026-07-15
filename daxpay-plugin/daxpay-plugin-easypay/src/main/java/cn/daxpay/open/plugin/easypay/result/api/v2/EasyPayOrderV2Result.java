package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付 V2 订单查询响应
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V2订单查询响应")
public class EasyPayOrderV2Result {

    /// 返回状态码（0 成功）
    @Schema(description = "返回状态码")
    @JsonProperty("code")
    private Integer code;

    /// 错误信息
    @Schema(description = "错误信息")
    @JsonProperty("msg")
    private String msg;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 通道订单号
    @Schema(description = "通道订单号")
    @JsonProperty("api_trade_no")
    private String apiTradeNo;

    /// 协议支付方式
    @Schema(description = "支付方式")
    @JsonProperty("type")
    private String type;

    /// 协议状态 0待付 1成功
    @Schema(description = "支付状态 0待付 1成功")
    @JsonProperty("status")
    private Integer status;

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    @JsonProperty("pid")
    private Integer pid;

    /// 订单创建时间
    @Schema(description = "订单创建时间")
    @JsonProperty("addtime")
    private String addtime;

    /// 订单完成时间
    @Schema(description = "订单完成时间")
    @JsonProperty("endtime")
    private String endtime;

    /// 商品名称
    @Schema(description = "商品名称")
    @JsonProperty("name")
    private String name;

    /// 金额（元，字符串）
    @Schema(description = "金额（元）")
    @JsonProperty("money")
    private String money;

    /// 已退款金额（元，字符串）
    @Schema(description = "已退款金额（元）")
    @JsonProperty("refundmoney")
    private String refundmoney;

    /// 业务扩展参数
    @Schema(description = "业务扩展参数")
    @JsonProperty("param")
    private String param;

    /// 支付用户标识
    @Schema(description = "支付用户标识")
    @JsonProperty("buyer")
    private String buyer;

    /// 客户端 IP
    @Schema(description = "客户端IP")
    @JsonProperty("clientip")
    private String clientip;

    /// 当前时间戳（秒）
    @Schema(description = "当前时间戳")
    @JsonProperty("timestamp")
    private String timestamp;

    /// 签名
    @Schema(description = "签名")
    @JsonProperty("sign")
    private String sign;

    /// 签名类型（RSA）
    @Schema(description = "签名类型")
    @JsonProperty("sign_type")
    private String signType;

}
