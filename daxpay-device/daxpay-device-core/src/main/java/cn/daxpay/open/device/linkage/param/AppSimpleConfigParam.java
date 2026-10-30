package cn.daxpay.open.device.linkage.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 应用设备联动配置保存参数(运营端)
///
/// 极简三件套: 播报/打印两个布尔 + 打印版式代码(内置版式枚举扩展位, 空=standard);
/// 无规则集、无线上单静音托管开关(静音在消费侧按订单来源固定判断)。
@Data
@Accessors(chain = true)
@Schema(title = "应用设备联动配置保存参数(运营端)")
public class AppSimpleConfigParam {

    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    private String appId;

    @Schema(description = "语音播报开关")
    @NotNull(message = "{validation.field.broadcastEnable.notNull}")
    private Boolean broadcastEnable;

    @Schema(description = "小票打印开关")
    @NotNull(message = "{validation.field.printEnable.notNull}")
    private Boolean printEnable;

    @Schema(description = "门店订单严格匹配(true=仅推送绑定该门店的设备, 未绑门店设备不参与; 空/false=未绑门店设备视为商户通用照常推送)")
    private Boolean storeStrictMatch;

    @Schema(description = "打印版式代码(内置版式, 空=standard)")
    @Size(max = 32, message = "{validation.field.templateCode.size}")
    private String templateCode;
}
