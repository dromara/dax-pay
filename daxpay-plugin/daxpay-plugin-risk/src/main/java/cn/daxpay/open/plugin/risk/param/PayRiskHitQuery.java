package cn.daxpay.open.plugin.risk.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 风险命中查询参数
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "风险命中查询参数")
public class PayRiskHitQuery {

    @Schema(description = "阶段")
    private String phase;

    @Schema(description = "命中类型")
    private String hitType;

    @Schema(description = "命中值")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String hitValue;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "来源场景")
    private String scene;

    @Schema(description = "创建时间起")
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime createTimeStart;

    @Schema(description = "创建时间止")
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime createTimeEnd;
}
