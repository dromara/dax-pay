package org.dromara.daxpay.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 支付通用响应参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@NoArgsConstructor
@Schema(title = "支付通用响应参数")
public class DaxResult<T>{

    /** 状态码 */
    @Schema(description = "状态码")
    private int code;

    /** 提示信息 */
    @Schema(description = "提示信息")
    private String msg;

    /** 业务内容 */
    @Schema(description = "业务内容")
    private T data;

    /** 签名 */
    @Schema(description = "签名")
    private String sign;

    @Schema(description = "响应时间")
    private LocalDateTime resTime;

    /** 追踪ID */
    @Schema(description = "追踪ID")
    private String traceId;

    public DaxResult(int successCode, T data, String msg) {
        this.code = successCode;
        this.data = data;
        this.msg = msg;
    }

    public DaxResult(int successCode, String msg) {
        this.code = successCode;
        this.msg = msg;
    }
}
