package cn.daxpay.open.platform.capability.audit.log.entity;

import cn.daxpay.open.platform.capability.audit.log.convert.LogConvert;
import cn.daxpay.open.platform.capability.audit.log.result.UnipayApiLogResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 统一支付接口审计日志
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "starter_audit_unipay_log", autoResultMap = true)
public class UnipayApiLogDb extends MpIdEntity implements ToResult<UnipayApiLogResult> {

    /// 商户号
    private String mchNo;

    /// 请求 ID
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

    /// 接入 IP 归属地
    private String requestLocation;

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

    /// 请求参数（脱敏后）
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String reqParam;

    /// 响应体（脱敏后）
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String resBody;

    /// 操作时间 (UTC)
    private OffsetDateTime operateTime;

    @Override
    public UnipayApiLogResult toResult() {
        return LogConvert.CONVERT.convert(this);
    }
}
