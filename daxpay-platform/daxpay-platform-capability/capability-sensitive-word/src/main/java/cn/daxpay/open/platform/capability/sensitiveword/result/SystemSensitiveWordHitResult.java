package cn.daxpay.open.platform.capability.sensitiveword.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 敏感词命中结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "敏感词命中结果")
public class SystemSensitiveWordHitResult extends BaseResult {

    @Schema(description = "词库ID")
    private Long wordId;

    @Schema(description = "命中词")
    private String hitWord;

    @Schema(description = "原文摘要")
    private String contentPreview;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "请求路径")
    private String requestPath;

    @Schema(description = "备注")
    private String remark;
}

