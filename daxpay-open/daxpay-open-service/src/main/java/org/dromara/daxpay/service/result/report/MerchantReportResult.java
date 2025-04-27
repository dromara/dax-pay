package org.dromara.daxpay.service.result.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 首页商户数量统计信息
 * @author xxm
 * @since 2024/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "首页商户数量统计信息")
public class MerchantReportResult {

    @Schema(description = "普通商户数量")
    private Integer normalCount;

    @Schema(description = "特约商户数量")
    private Integer partnerCount;

    @Schema(description = "普通商户应用数量")
    private Integer normalAppCount;

    @Schema(description = "特约商户应用数量")
    private Integer partnerAppCount;
}
