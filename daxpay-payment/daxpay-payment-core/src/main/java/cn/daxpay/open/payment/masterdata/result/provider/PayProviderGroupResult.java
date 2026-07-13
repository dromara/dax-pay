package cn.daxpay.open.payment.masterdata.result.provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 按支付渠道分组（管理端）
@Data
@Accessors(chain = true)
@Schema(title = "支付渠道分组")
public class PayProviderGroupResult {

    @Schema(description = "支付渠道编码")
    private String provider;

    @Schema(description = "支付渠道展示名")
    private String providerLabel;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sortNo;

    @Schema(description = "是否启用")
    private boolean enabled;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "渠道下支付方式列表")
    private List<PayProviderMethodResult> methods;
}