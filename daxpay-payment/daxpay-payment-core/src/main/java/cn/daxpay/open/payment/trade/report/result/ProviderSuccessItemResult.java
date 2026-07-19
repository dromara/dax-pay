package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付渠道成功率单项结果
///
/// 一个支付渠道(provider code)对应的支付成功率(百分比).
/// 口径: success_count / total_count * 100, total_count 含所有非初始化态(success/fail/close/cancel).
@Data
@Accessors(chain = true)
@Schema(title = "支付渠道成功率单项")
public class ProviderSuccessItemResult {

    /// 支付渠道编码
    @Schema(description = "支付渠道编码")
    private String provider;

    /// 成功率(0-100, 保留 1 位小数)
    @Schema(description = "成功率百分比")
    private Double rate;
}
