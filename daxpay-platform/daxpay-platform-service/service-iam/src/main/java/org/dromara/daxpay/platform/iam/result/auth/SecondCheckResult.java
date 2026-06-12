package org.dromara.daxpay.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 二次校验信息
///
@Data
@Accessors(chain = true)
@Schema(title = "二次校验信息")
public class SecondCheckResult {

    @Schema(description = "是否需要二次校验")
    private boolean required;

    @Schema(description = "二次校验类型")
    private String type;

    @Schema(description = "二次校验说明")
    private String message;

}
