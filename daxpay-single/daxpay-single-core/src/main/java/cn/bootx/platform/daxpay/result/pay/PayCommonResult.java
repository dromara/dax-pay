package cn.bootx.platform.daxpay.result.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付通用返回参数
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Schema(title = "")
public class PayCommonResult {

    @Schema(description = "请求ID")
    private String reqId;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String extraParam;
}
