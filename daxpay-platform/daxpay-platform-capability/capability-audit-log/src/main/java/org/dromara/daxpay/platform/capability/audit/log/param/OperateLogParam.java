package org.dromara.daxpay.platform.capability.audit.log.param;

import org.dromara.daxpay.platform.core.annotation.PartialMaskRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 操作日志
///
@Data
@Accessors(chain = true)
@Schema(description = "操作日志")
public class OperateLogParam {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "操作人员id")
    private Long operateId;

    @Schema(description = "操作人员账号")
    private String account;

    @Schema(description = "终端编码")
    private String client;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "请求url")
    private String operateUrl;

    @Schema(description = "操作ip")
    private String operateIp;

    @Schema(description = "操作地点")
    private String operateLocation;

    @Schema(description = "请求参数")
    private String operateParam;

    @Schema(description = "返回参数")
    private String operateReturn;

    @Schema(description = "操作状态（0正常 1异常）")
    private Boolean success;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间 (UTC)")
    private OffsetDateTime operateTime;

    /// ==================== 注解配置字段（AOP采集时使用） ====================

    @Schema(description = "是否保存请求参数")
    private Boolean saveParam;

    @Schema(description = "是否保存返回参数")
    private Boolean saverReturn;

    @Schema(description = "是否对请求参数进行脱敏")
    private Boolean maskParam;

    @Schema(description = "是否对返回参数进行脱敏")
    private Boolean maskReturn;

    @Schema(description = "全量脱敏键名单")
    private String[] fullMaskKeys;

    @Schema(description = "部分脱敏规则")
    private PartialMaskRule[] partialMaskRules;

    @Schema(description = "参数/返回值最大长度")
    private Integer payloadMaxLength;

}
