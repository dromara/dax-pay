package cn.daxpay.open.platform.capability.sensitiveword.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词试检参数
///
@Data
@Accessors(chain = true)
@Schema(title = "敏感词试检参数")
public class SensitiveWordCheckTextParam {

    @Schema(description = "待检文本")
    @NotBlank(message = "{validation.field.content.notBlank}")
    @Size(max = 2000, message = "{validation.field.content.size}")
    private String text;

    @Schema(description = "是否写入命中审计")
    private Boolean recordHit;
}

