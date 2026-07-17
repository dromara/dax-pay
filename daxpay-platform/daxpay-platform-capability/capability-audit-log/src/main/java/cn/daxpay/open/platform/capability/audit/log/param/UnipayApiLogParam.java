package cn.daxpay.open.platform.capability.audit.log.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 统一支付接口审计日志写入参数
///
@Data
@Accessors(chain = true)
@Schema(description = "统一支付接口审计日志写入参数")
public class UnipayApiLogParam {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "请求ID")
    private String reqId;

    @Schema(description = "接口路径")
    private String apiPath;

    @Schema(description = "接口标题")
    private String apiTitle;

    @Schema(description = "HTTP 方法")
    private String requestMethod;

    @Schema(description = "商户入参声明的客户端 IP")
    private String clientIp;

    @Schema(description = "真实接入 IP")
    private String requestIp;

    @Schema(description = "接入 IP 归属地")
    private String requestLocation;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "业务错误码")
    private Integer errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "耗时（毫秒）")
    private Long durationMs;

    @Schema(description = "链路追踪 ID")
    private String traceId;

    @Schema(description = "请求参数 JSON")
    private String reqParam;

    @Schema(description = "响应体 JSON")
    private String resBody;

    @Schema(description = "操作时间 (UTC)")
    private OffsetDateTime operateTime;
}
