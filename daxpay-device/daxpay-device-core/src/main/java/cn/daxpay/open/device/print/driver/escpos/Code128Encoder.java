package cn.daxpay.open.device.print.driver.escpos;

import cn.daxpay.open.device.print.template.engine.BarcodeCodec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/// # CODE128 条码数据编码(纯函数)
///
/// 生成 ESC/POS `GS k 73`(函数 B 语法)所需的**带代码集前缀的数据串**, 校验位由打印机固件计算。
/// 形态由 [BarcodeCodec] 统一判定(单代码集二选一: 全串纯数字偶数 → {C, 其余 → {B 整串,
/// plus 2026-10-24 真机翻车后回退段内切换)—— 形态同时是合成层几何守卫的宽度依据, 两处必须同源。
/// B 段字面 `{` 按 Epson 语法转义为 `{{`; C 段纯数字无转义。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Code128Encoder {

    /// 文本 → `GS k 73` 数据串(含 {B/{C 前缀)
    public static String toEscPosData(String content) {
        if (content.isEmpty()) {
            throw new IllegalArgumentException("条码内容不能为空");
        }
        if (content.length() > 200) {
            throw new IllegalArgumentException("条码内容过长");
        }
        if (BarcodeCodec.usesCodeC(content)) {
            return "{C" + content;
        }
        return "{B" + content.replace("{", "{{");
    }
}
