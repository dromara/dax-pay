package cn.daxpay.open.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户风控配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户风控配置参数")
public class MchRiskConfigParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 是否启用地理围栏
    @Schema(description = "是否启用地理围栏")
    private Boolean geoFenceEnabled;
}
