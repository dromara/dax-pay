package cn.daxpay.open.platform.capability.audit.log.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 统一支付接口审计日志查询参数
///
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(description = "统一支付接口审计日志查询参数")
public class UnipayApiLogQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "请求ID")
    private String reqId;

    @Schema(description = "接口路径")
    private String apiPath;

    @Schema(description = "接口标题")
    private String apiTitle;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否成功")
    private Boolean success;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "链路追踪 ID")
    private String traceId;

    @Schema(description = "真实接入 IP")
    private String requestIp;
}
