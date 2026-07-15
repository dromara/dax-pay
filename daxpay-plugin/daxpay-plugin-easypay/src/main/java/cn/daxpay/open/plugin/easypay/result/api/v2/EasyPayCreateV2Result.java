package cn.daxpay.open.plugin.easypay.result.api.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付 V2 统一下单响应
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V2统一下单响应")
public class EasyPayCreateV2Result {

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

    /// 发起支付类型（qrcode/jsapi/html 等）
    @Schema(description = "发起支付类型")
    @JsonProperty("pay_type")
    private String payType;

    /// 发起支付参数
    @Schema(description = "发起支付参数")
    @JsonProperty("pay_info")
    private String payInfo;

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
