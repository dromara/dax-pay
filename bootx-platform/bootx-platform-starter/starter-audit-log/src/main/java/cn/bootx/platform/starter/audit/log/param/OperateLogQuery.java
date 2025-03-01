package cn.bootx.platform.starter.audit.log.param;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作日志查询参数
 * @author xxm
 * @since 2025/2/12
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(description = "操作日志")
public class OperateLogQuery {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "操作人员账号")
    private String account;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "终端编码")
    private String client;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "业务类型")
    private String businessType;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "请求方法")
    private String method;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "请求url")
    private String operateUrl;

    @Schema(description = "操作ip")
    private String operateIp;

    @Schema(description = "操作地点")
    private String operateLocation;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "操作状态")
    private Boolean success;

}
