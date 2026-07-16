package cn.daxpay.open.platform.capability.sensitiveword.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 敏感词结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "敏感词结果")
public class SystemSensitiveWordResult extends BaseResult {

    @Schema(description = "敏感词")
    private String word;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "匹配模式")
    private String matchMode;

    @Schema(description = "处理级别")
    private String level;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}

