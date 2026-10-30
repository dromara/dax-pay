package cn.daxpay.open.device.virtual.enums;

/// # 虚拟设备试播/试打模式
///
/// 试打内容一致(同一份平台渲染产物), 差异只在"发什么指令 + 是否真出纸":
/// - [ANNOUNCE] 试播: 仅 audio.test, 不涉打印(试播接口专用)
/// - [PRINT_BOTH] 播报+打印: pay.success 携带 printData, 附 test 标记(终端不记账)
/// - [PRINT_ONLY] 仅打印: print.ticket 纯打印指令, 附 manual 标记(终端绕过自动打印开关)
///
/// 移植自 plus `daxpay-device-core`(快照 773134af): 开源版无试打预览独立端点
/// (PREVIEW_ONLY 不移植, 试打弹窗预览由前端 C1 波以渲染结果承接)。
public enum VirtualTestMode {

    /// 试播(纯语音)
    ANNOUNCE,

    /// 播报 + 打印
    PRINT_BOTH,

    /// 仅打印(不播报)
    PRINT_ONLY
}
