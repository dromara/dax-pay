package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台敏感词策略结果
///
@Data
@Accessors(chain = true)
@Schema(title = "平台敏感词策略结果")
public class PlatformSensitiveWordConfigResult {

    @Schema(description = "是否启用敏感词过滤")
    private Boolean enabled;

    @Schema(description = "是否回显命中词")
    private Boolean revealWord;

    @Schema(description = "是否写入命中审计")
    private Boolean recordHit;

    @Schema(description = "原文摘要最大长度")
    private Integer contentPreviewMaxLen;
}
