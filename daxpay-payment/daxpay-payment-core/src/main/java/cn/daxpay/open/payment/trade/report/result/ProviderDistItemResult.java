package cn.daxpay.open.payment.trade.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付渠道分布单项结果
///
/// 一个支付渠道(provider code)对应的成交金额与笔数, 前端按金额计算占比绘制饼图。
/// provider 为支付渠道编码(如 wechat/alipay/union_pay), 前端通过渠道 i18n 映射展示名称。
@Data
@Accessors(chain = true)
@Schema(title = "支付渠道分布单项")
public class ProviderDistItemResult {

    /// 支付渠道编码
    @Schema(description = "支付渠道编码")
    private String provider;

    /// 成交金额(分)
    @Schema(description = "成交金额(分)")
    private Long amount;

    /// 成交笔数
    @Schema(description = "成交笔数")
    private Long count;
}
