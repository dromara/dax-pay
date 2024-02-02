package cn.bootx.platform.daxpay.result;

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
public class CommonResult {

    @Schema(description = "追踪ID")
    private String traceId = MDC.get(CommonCode.TRACE_ID);
}
