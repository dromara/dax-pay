package cn.daxpay.open.device.print.template.engine.model;

/// # 渲染结果
///
/// [LineModel] 与其编译出的 ESC/POS 指令流: 模板层止步于行模型,
/// 字节流由编排层(print.service)编译后随本记录下发, 预览与打印两出口同源。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public record RenderResult(LineModel lineModel, byte[] escpos) {

    /// ESC/POS 指令流 base64(推送链路与真机试打用)
    public String escposBase64() {
        return encodeBase64(escpos);
    }

    /// 字节流 base64 编码(行模型直编译出口共用)
    public static String encodeBase64(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
}
