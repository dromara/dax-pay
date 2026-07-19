package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户交易额排名单项结果
///
/// 按商户维度聚合的成交金额排名, 金额单位分, 商户名取自 mch_base.name(JOIN).
/// proportion(占比百分比) 由 Service 层按总额计算, 保留 1 位小数。
@Data
@Accessors(chain = true)
@Schema(title = "商户交易额排名单项")
public class MerchantRankItemResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称(JOIN mch_base.name)
    @Schema(description = "商户名称")
    private String merchantName;

    /// 成交金额(分)
    @Schema(description = "成交金额(分)")
    private Long amount;

    /// 成交笔数
    @Schema(description = "成交笔数")
    private Long orders;

    /// 占比百分比(0-100, 保留 1 位小数)
    @Schema(description = "占比百分比")
    private Double proportion;
}
