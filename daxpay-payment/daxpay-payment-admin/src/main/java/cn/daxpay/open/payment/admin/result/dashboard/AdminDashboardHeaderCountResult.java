package cn.daxpay.open.payment.admin.result.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 运营工作台头部计数
@Data
@Accessors(chain = true)
@Schema(title = "运营工作台头部计数")
public class AdminDashboardHeaderCountResult {

    /// 商户总数
    @Schema(description = "商户总数")
    private Long merchantCount;

    /// 通道商户总数
    @Schema(description = "通道商户总数")
    private Long channelMerchantCount;

    /// 运营端用户数(不含超管)
    @Schema(description = "运营端用户数")
    private Long userCount;
}
