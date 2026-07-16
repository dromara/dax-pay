package cn.daxpay.open.platform.capability.sensitiveword.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词查询
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "敏感词查询")
public class SystemSensitiveWordQuery {

    @Schema(description = "敏感词")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String word;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "匹配模式")
    private String matchMode;
}

