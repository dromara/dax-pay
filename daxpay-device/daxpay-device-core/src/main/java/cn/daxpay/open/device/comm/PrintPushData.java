package cn.daxpay.open.device.comm;

/// # 小票推送载荷(双流)
///
/// 平台侧按设备纸宽渲染出的 ESC/POS 指令流(Base64)与纸宽, 随 pay.success 信封 data 的
/// `printData` 字段下发到虚拟设备; APP 解码后直接写入本地蓝牙/USB transport(哑管道, 零本地渲染)。
/// 与语音播报共用同一条消息: 一次支付只下发一个 msgId, 避免打印独立消息被 APP 当成二次播报。
///
/// ## 双流备宽
///
/// 虚拟设备的实际纸宽平台侧不可知(APP 本地设置才是真相), 台账宽度非 80 时同时下发
/// 58mm 主流([escposBase64])与 80mm 备流([escposBase64W80]), 终端按本地纸宽设置挑流;
/// 台账已是 80 则单流(台账权威)。备流为 null(台账 80 或备宽渲染失败)时旧形状不变,
/// 旧终端不识别新字段照用主流, 协议向后兼容。
public record PrintPushData(String escposBase64, int paperWidth, String escposBase64W80) {

    /// 单流构造(台账宽度已知的设备: 台账 80 无需备宽流; 测试桩用)
    public PrintPushData(String escposBase64, int paperWidth) {
        this(escposBase64, paperWidth, null);
    }
}
