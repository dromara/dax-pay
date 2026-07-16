package cn.daxpay.open.platform.capability.sensitiveword.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词参数
///
@Data
@Accessors(chain = true)
@Schema(title = "敏感词参数")
public class SystemSensitiveWordParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "敏感词")
    @NotBlank(message = "{validation.field.word.notBlank}", groups = ValidationGroup.add.class)
    @Size(max = 64, message = "{validation.field.word.size}")
    private String word;

    @Schema(description = "分类")
    @Size(max = 32, message = "{validation.field.category.size}")
    private String category;

    @Schema(description = "匹配模式")
    @Size(max = 16, message = "{validation.field.matchMode.size}")
    private String matchMode;

    @Schema(description = "状态")
    @Size(max = 16, message = "{validation.field.status.size}")
    private String status;

    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}

