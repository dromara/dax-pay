package cn.daxpay.open.device.print.template.engine;

import cn.daxpay.open.device.print.template.engine.model.LineModel;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/// # 小票行合成器单元测试(二维码缩码口径)
///
/// 锁定 2026-10-27 口径: 二维码目标宽统一按 58mm 物理口径 342 点,
/// 58mm 与 80mm 两档纸宽落纸尺寸一致(80mm 封顶 16 不触顶)。
/// 移植自 plus `PrintLineComposerTest#testQrModuleDots`, 仅保留二维码尺寸断言。
class PrintLineComposerTest {

    @Test
    @DisplayName("二维码: 4 字符走 v1(29 模块含静区), 58/80mm 同物理目标宽均 11 点")
    void testQrModuleDots() {
        PrintTemplateElement.QrElement qr = new PrintTemplateElement.QrElement("1234");
        LineModel.QrLine at58 = assertInstanceOf(LineModel.QrLine.class,
                PrintLineComposer.compose(List.of(qr), 58).lines().getFirst());
        LineModel.QrLine at80 = assertInstanceOf(LineModel.QrLine.class,
                PrintLineComposer.compose(List.of(qr), 80).lines().getFirst());
        // 目标宽统一 58mm 物理口径 342 dots: 342/29=11, 两档纸宽落纸尺寸一致;
        // 原生指令无传输量约束, 上限只控票面高度, 见 QrCodec 类注释
        assertEquals(11, at58.moduleDots());
        assertEquals(11, at80.moduleDots());
    }
}
