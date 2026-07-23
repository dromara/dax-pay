package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户端维度交易额排名单项
///
/// 在本商户下按通道商户 / 应用 / 门店聚合; 金额单位分。
/// proportion 由 Service 按返回列表总额计算。
@Data
@Accessors(chain = true)
@Schema(title = "维度交易额排名单项")
public class DimRankItemResult {

    /// 维度键(channelMchNo / appId / storeNo; 未指定门店为空串)
    @Schema(description = "维度键")
    private String dimKey;

    /// 展示名(JOIN 得到; 门店未指定时为 null, 由前端 i18n)
    @Schema(description = "展示名")
    private String dimName;

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
