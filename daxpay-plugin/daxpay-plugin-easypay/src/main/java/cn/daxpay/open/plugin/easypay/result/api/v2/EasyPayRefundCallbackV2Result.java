package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/// # 易支付 V2 退款回调报文
///
/// 退款成功出站通知信封, GET query 参数形式投递至原支付单的 notify_url;
/// 协议无退款通知标准字段, 此处为插件扩展定义: money 为退款金额(非订单金额)。
@Data
@Accessors(chain = true)
@Schema(title = "易支付V2退款回调报文")
public class EasyPayRefundCallbackV2Result implements Serializable {

    @JsonProperty("pid")
    private Integer pid;

    /// 原支付单平台单号
    @JsonProperty("trade_no")
    private String tradeNo;

    /// 原支付单商户订单号
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /// 平台退款单号
    @JsonProperty("refund_no")
    private String refundNo;

    /// 商户退款单号
    @JsonProperty("out_refund_no")
    private String bizRefundNo;

    /// 原支付方式
    @JsonProperty("type")
    private String type;

    /// 商品名称
    @JsonProperty("name")
    private String name;

    /// 退款金额(元)
    @JsonProperty("money")
    private String money;

    @JsonProperty("trade_status")
    private String tradeStatus;

    @JsonProperty("addtime")
    private String addTime;

    @JsonProperty("endtime")
    private String endTime;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("sign")
    private String sign;

    @JsonProperty("sign_type")
    private String signType;
}
