package cn.daxpay.open.device.print.template.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/// # CODE128 编码形态与模块数(纯函数)
///
/// 供两处消费, 必须同源: 合成层几何守卫([PrintLineComposer])按模块数选模块宽,
/// ESC/POS 驱动层([cn.daxpay.open.device.print.driver.escpos.Code128Encoder])按形态产出数据串。
///
/// ## 形态只做「单代码集」二选一(plus 2026-10-24 真机翻车后回退, 兼容优先)
/// 全串纯数字且偶数位 → `{C`(两位数字一对, 模块数减半); 其余 → 整串 `{B`。
/// 段内 `{B...{C...` 混合切换虽是 ESC/POS 标准语法(Epson 原生机支持), 但杂牌固件
/// 普遍只实现开头前缀, 段中再切会**静默丢弃整个条码段**(真机实测落纸无条码),
/// 生产蓝本 dax-pos-elf 也只做二选一 —— 混合单号的宽度收益只能走条码位图化另立项。
///
/// ## 模块数公式(与输出形态一致)
/// START(11) + 数据符号×11 + 校验(11) + STOP(13):
/// B 形态每字符 1 符号(= 11n+35); C 形态每两位数字 1 符号。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BarcodeCodec {

    /// 内容 → 是否走 Code C(全串纯数字且偶数位)
    public static boolean usesCodeC(String content) {
        if (content.length() % 2 != 0) {
            return false;
        }
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /// 内容 → CODE128 符号总模块数(START + 数据 + 校验 + STOP, 与输出形态一致)
    public static int moduleCount(String content) {
        int symbols = usesCodeC(content) ? content.length() / 2 : content.length();
        return 11 * (1 + symbols + 1) + 13;
    }
}
