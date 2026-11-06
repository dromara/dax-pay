package cn.daxpay.open.payment.admin.result.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户设备与应用计数(运营端查看指定商户)
///
/// 供商户工作台「收银与设备」「支付配置」磁贴数字角标使用, 计数口径一律存量总数:
/// 只过滤逻辑删除, 不按启用/激活状态过滤; 平台级共享资源(平台库存/未绑定设备)不计入。
/// 字段刻意用 Integer 而非 Long, 规避全局 Long→字符串序列化契约(同工作台头部计数口径)。
@Data
@Accessors(chain = true)
@Schema(title = "商户设备与应用计数")
public class AdminMchDeviceCountResult {

    /// 名下逻辑终端数(系统终端台账, 不限启用态)
    @Schema(description = "名下逻辑终端数")
    private Integer terminalCount;

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
