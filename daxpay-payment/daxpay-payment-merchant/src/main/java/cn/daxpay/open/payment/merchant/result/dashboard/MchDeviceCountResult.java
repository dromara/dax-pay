package cn.daxpay.open.payment.merchant.result.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户工作台设备与应用计数
///
/// 聚合商户端应用工作台「通知与设备」等卡片的计数,
/// 由 [cn.daxpay.open.payment.merchant.service.dashboard.MchDashboardDeviceService#deviceCounts] 组装。
/// 计数字段一律用 Integer(全局 Long/long 序列化为字符串契约, 前端按 number 接收)。
@Data
@Accessors(chain = true)
@Schema(title = "商户工作台设备与应用计数")
public class MchDeviceCountResult {

    /// 名下虚拟设备台数(已绑定商户, 不含平台库存/未绑定设备)
    @Schema(description = "名下虚拟设备台数(不含平台库存/未绑定设备)")
    private Integer virtualDeviceCount;

    /// 商户微信应用数(公众号/小程序等开放应用)
    @Schema(description = "商户微信应用数")
    private Integer wxAppCount;

    /// 商户抖音应用数(小程序/移动应用/网页应用等开放应用)
    @Schema(description = "商户抖音应用数")
    private Integer dyAppCount;
}
