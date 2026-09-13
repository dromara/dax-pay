package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 连通性检查结果
///
@Data
@Accessors(chain = true)
@Schema(title = "连通性检查结果")
public class ConnectivityCheckResult {

    /// 是否成功
    @Schema(description = "是否成功")
    private boolean success;

    /// 提示信息(已国际化)
    @Schema(description = "提示信息")
    private String message;

    /// HTTP 状态码(端点探测时有值)
    @Schema(description = "HTTP 状态码")
    private Integer statusCode;

    /// 耗时毫秒
    @Schema(description = "耗时毫秒")
    private Long latencyMs;

    /// 警告信息(已国际化, 检查通过但存在不影响结论的隐患时有值, 如公开访问地址不可达)
    @Schema(description = "警告信息")
    private String warning;

    public static ConnectivityCheckResult ok(String message, Long latencyMs, Integer statusCode) {
        return ok(message, latencyMs, statusCode, null);
    }

    public static ConnectivityCheckResult ok(String message, Long latencyMs, Integer statusCode, String warning) {
        return new ConnectivityCheckResult()
                .setSuccess(true)
                .setMessage(message)
                .setLatencyMs(latencyMs)
                .setStatusCode(statusCode)
                .setWarning(warning);
    }

    public static ConnectivityCheckResult fail(String message, Long latencyMs, Integer statusCode) {
        return new ConnectivityCheckResult()
                .setSuccess(false)
                .setMessage(message)
                .setLatencyMs(latencyMs)
                .setStatusCode(statusCode);
    }

    public static ConnectivityCheckResult fail(String message) {
        return fail(message, null, null);
    }
}
