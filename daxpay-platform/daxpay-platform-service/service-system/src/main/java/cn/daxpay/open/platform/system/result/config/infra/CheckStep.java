package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 连通性检查单步结果
///
/// 以语义标识描述检查步骤(前端据此渲染步骤名称), 状态与说明由后端给出.
@Data
@Accessors(chain = true)
@Schema(title = "连通性检查单步结果")
public class CheckStep {

    /// 状态: 通过
    public static final String STATUS_SUCCESS = "success";

    /// 状态: 失败
    public static final String STATUS_FAILED = "failed";

    /// 状态: 警告(不影响整体结论, 如公开访问地址在服务端不可达)
    public static final String STATUS_WARNING = "warning";

    /// 状态: 跳过(前置步骤失败或检查条件不满足, 未执行)
    public static final String STATUS_SKIPPED = "skipped";

    /// 步骤标识: connect-连接与桶存在 / write-写入 / read-读取 / publicAccess-公开访问
    @Schema(description = "步骤标识")
    private String step;

    /// 步骤状态: success / failed / warning / skipped
    @Schema(description = "步骤状态")
    private String status;

    /// 失败或警告说明(已国际化, 通过与跳过时为空)
    @Schema(description = "失败或警告说明")
    private String message;

    public static CheckStep of(String step, String status) {
        return of(step, status, null);
    }

    public static CheckStep of(String step, String status, String message) {
        return new CheckStep()
                .setStep(step)
                .setStatus(status)
                .setMessage(message);
    }
}
