package cn.daxpay.open.payment.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/// # 支付通用响应参数
///
@Data
@NoArgsConstructor
@Schema(title = "支付通用响应参数")
public class DaxResult<T>{

    /// 状态码
    @Schema(description = "状态码")
    private int code;

    /// 提示信息
    @Schema(description = "提示信息")
    private String msg;

    /// 业务内容
    @Schema(description = "业务内容")
    private T data;

    /// 签名
    @Schema(description = "签名")
    private String sign;

    @Schema(description = "响应时间(UTC)")
    private OffsetDateTime resTime;

    /// 请求ID（回显入参 reqId）
    @Schema(description = "请求ID")
    private String reqId;

    /// 追踪ID
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
