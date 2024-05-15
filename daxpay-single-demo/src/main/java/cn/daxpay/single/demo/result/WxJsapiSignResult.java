package cn.daxpay.single.demo.result;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信Jsapi预支付签名返回信息
 * @author xxm
 * @since 2024/2/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信Jsapi预支付签名返回信息")
public class WxJsapiSignResult {
    @Schema(description = "公众号ID")
    private String appId;
    @Schema(description = "时间戳(秒)")
    private String timeStamp;
    @Schema(description = "随机串")
    private String nonceStr;
    @Alias("package")
    @JsonProperty("package")
    @Schema(description = "预支付ID, 已经是 prepay_id=xxx 格式")
    private String prePayId;
    @Schema(description = "签名类型")
    private String signType;
    @Schema(description = "签名方式")
    private String paySign;
}
