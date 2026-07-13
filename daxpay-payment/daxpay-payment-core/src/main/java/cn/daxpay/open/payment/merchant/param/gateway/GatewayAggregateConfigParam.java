package cn.daxpay.open.payment.merchant.param.gateway;

import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/// # 网关聚合扫码配置参数
///
/// level 控制场景子表的填充要求:
/// - AUTO: scenes 可为空
/// - METHOD: 每场景填 method
/// - DIRECT: 每场景填 channelMchNo + capability
@Data
@Schema(title = "网关聚合扫码配置参数")
public class GatewayAggregateConfigParam {

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    /// @see AggregateConfigLevelEnum
    @Schema(description = "配置深度: auto/method/direct")
    @NotBlank(message = "{validation.field.level.notBlank}")
    @Size(max = 32, message = "{validation.field.level.size}")
    private String level;

    @Schema(description = "是否自动拉起支付")
    private Boolean autoLaunch;

    @Valid
    @Schema(description = "场景配置列表")
    private List<GatewayAggregateSceneParam> scenes;
}
