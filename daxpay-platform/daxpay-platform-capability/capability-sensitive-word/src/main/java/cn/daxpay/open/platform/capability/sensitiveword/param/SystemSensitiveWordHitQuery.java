package cn.daxpay.open.platform.capability.sensitiveword.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词命中查询
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "敏感词命中查询")
public class SystemSensitiveWordHitQuery {

    @Schema(description = "命中词")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String hitWord;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "商户号")
    private String mchNo;
}

