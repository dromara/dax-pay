package cn.daxpay.open.device.linkage.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 应用设备联动配置(极简聚合视图)
///
/// 播报/打印两个布尔 + 打印版式代码; 行不存在时按默认全开(播报开/打印开/standard)派生,
/// 不落库。
@Data
@Accessors(chain = true)
@Schema(title = "应用设备联动配置")
public class AppSimpleConfigResult {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "语音播报开关")
    private Boolean broadcastEnable;

    @Schema(description = "小票打印开关")
    private Boolean printEnable;

    @Schema(description = "门店订单严格匹配(true=仅推送绑定该门店的设备; false=未绑门店设备视为商户通用照常推送)")
    private Boolean storeStrictMatch;

    @Schema(description = "打印版式代码(内置版式, 默认 standard)")
    private String templateCode;
}
