package cn.daxpay.open.plugin.easypay.result.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付 V1 统一下单响应
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付V1统一下单响应")
public class EasyPayCreateV1Result {

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

    /// 支付跳转链接
    @Schema(description = "支付跳转链接")
    @JsonProperty("payurl")
    private String payurl;

    /// 二维码内容
    @Schema(description = "二维码内容")
    @JsonProperty("qrcode")
    private String qrcode;

    /// URL Scheme
    @Schema(description = "URL Scheme")
    @JsonProperty("urlscheme")
    private String urlscheme;
}
