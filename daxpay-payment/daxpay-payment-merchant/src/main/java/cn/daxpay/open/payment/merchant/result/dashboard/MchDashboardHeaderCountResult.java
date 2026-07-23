package cn.daxpay.open.payment.merchant.result.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户工作台头部计数
@Data
@Accessors(chain = true)
@Schema(title = "商户工作台头部计数")
public class MchDashboardHeaderCountResult {

    /// 应用数
    @Schema(description = "应用数")
    private Long appCount;

    /// 门店数
    @Schema(description = "门店数")
    private Long storeCount;

    /// 通道商户数
    @Schema(description = "通道商户数")
    private Long channelMerchantCount;
}
