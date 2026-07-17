package cn.daxpay.open.platform.core.event;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 统一支付接口访问审计事件
///
/// 由开放支付验签切面发布，审计模块异步落库。纯 POJO，无 Spring 依赖。
/// 业务索引：`mchNo` + `reqId`；不单独索引 appId/业务单号。
@Data
@Accessors(chain = true)
public class UnipayApiAccessLogEvent {

    /// 商户号
    private String mchNo;

    /// 请求 ID（商户传入，审计主索引）
    private String reqId;

    /// 接口路径
    private String apiPath;

    /// 接口标题
    private String apiTitle;

    /// HTTP 方法
    private String requestMethod;

    /// 商户入参声明的客户端 IP
    private String clientIp;

    /// 真实接入 IP
    private String requestIp;

    /// 是否成功
    private Boolean success;

    /// 业务错误码
    private Integer errorCode;

    /// 错误信息
    private String errorMsg;

    /// 耗时（毫秒）
    private Long durationMs;

    /// 链路追踪 ID
    private String traceId;

    /// 请求参数 JSON（未脱敏，由审计模块强制脱敏）
    private String reqParam;

    /// 响应体 JSON（未脱敏，由审计模块强制脱敏）
    private String resBody;

    /// 操作时间 (UTC)
    private OffsetDateTime operateTime;
}
