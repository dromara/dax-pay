package cn.daxpay.open.payment.merchant.result.config;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户风控配置结果
///
/// 商户号 / 商户名称由父类 [MchBaseResult] 提供(mchNo + mchName @Trans 翻译)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户风控配置结果")
public class MchRiskConfigResult extends MchBaseResult {

    /// 是否启用地理围栏
    @Schema(description = "是否启用地理围栏")
    private Boolean geoFenceEnabled;
}
