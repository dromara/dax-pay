package cn.bootx.platform.daxpay.result.pay;

import cn.bootx.platform.common.core.code.CommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.slf4j.MDC;

/**
 * 支付通用返回参数
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Schema(title = "支付通用返回参数")
public class PayCommonResult {

    @Schema(description = "请求ID")
    private String reqId = MDC.get(CommonCode.TRACE_ID);

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String extraParam;
}
