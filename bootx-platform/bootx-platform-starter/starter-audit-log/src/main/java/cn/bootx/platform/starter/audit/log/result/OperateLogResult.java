package cn.bootx.platform.starter.audit.log.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Data
@Accessors(chain = true)
public class OperateLogResult {

    @Schema(description = "日志id")
    private Long id;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "操作人员id")
    private Long operateId;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "操作人员账号")
    private String account;

    @Schema(description = "登录终端")
    private String client;

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

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

}
