package cn.daxpay.open.device.print.driver.escpos;

import cn.daxpay.open.device.print.template.engine.PrintCharset;
import cn.daxpay.open.device.print.template.engine.PrintLineComposer;
import cn.daxpay.open.device.print.template.engine.model.LineModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/// # ESC/POS 指令编译器
///
/// [LineModel] → ESC/POS 字节流(58/80mm 通用)。移植自虚拟设备 APP `EscPosBuilder.kt`
/// (同源蓝本 dax-pos-elf `EscPosBuilder.cs`, C#→Kotlin→Java 两代移植), 指令集为其子集对齐:
/// 对齐/加粗/倍宽按**状态跟踪**只在变化时下发(与 APP 模板的指令习惯一致, 字节流更精简)。
///
/// ## 硬约束(不可违背)
/// - 中文统一 GB18030 编码(不可用时退 GBK), UTF-8 会乱码
/// - GBK 双字节字库外的字符先经 [PrintCharset#sanitize] 折叠, 否则四字节序列落纸成 ASCII 数字
/// - 两栏对齐用 ESC $ 绝对定位, 禁止空格 padding(全角 24 点/半角 12 点)
/// - 条码走 GS k 73(CODE128, 函数 B 语法长度前缀), HRI 在下方
/// - 二维码统一走 `GS ( k` 原生指令: 由固件按标准算法生成, 数据量与内容等长,
///   固定居中; 不提供 `GS v 0` 位图兜底 —— 不认原生指令的固件不在支持范围
/// - 58mm = 384 dots / 80mm = 576 dots
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EscPosCompiler {

    private static final Charset GB = runCatching("GB18030", "GBK");

    /// 对齐方式(对应 ESC a 参数)
    private enum AlignCode {
        LEFT(48), CENTER(49), RIGHT(50);

        final int code;

        AlignCode(int code) {
            this.code = code;
        }
    }

    private final ByteArrayOutputStream out = new ByteArrayOutputStream(2048);

    /// 指令状态跟踪(ESC @ 复位后的默认值)
    private String lastAlign = "left";
    private boolean lastBold = false;
    private boolean lastDoubleWidth = false;

    /// 原生二维码单次存数据上限(字节): 部分固件符号缓冲仅 256 字节, 取 200 留出参数余量
    private static final int QR_NATIVE_CHUNK = 200;

    /// 编译行模型为 ESC/POS 字节流
    public static byte[] compile(LineModel model) {
        EscPosCompiler compiler = new EscPosCompiler();
        compiler.write(0x1B, 0x40);
        for (LineModel.Line line : model.lines()) {
            compiler.compileLine(line, model.dotsPerLine());
        }
        return compiler.out.toByteArray();
    }

    private void compileLine(LineModel.Line line, int dotsPerLine) {
        switch (line) {
            case LineModel.TextLine l -> compileText(l);
            case LineModel.DividerLine l -> textLine(l.pattern().repeat(dotsPerLine / PrintLineComposer.NARROW_CHAR_DOTS));
            case LineModel.BarcodeLine l -> compileBarcode(l);
            case LineModel.QrLine l -> compileQr(l);
            case LineModel.FeedLine l -> {
                int lines = clamp(l.lines(), 0, 255);
                write(0x1B, 0x64, lines);
            }
            case LineModel.CutLine l -> write(0x1D, 0x56, 0x42, 0x03);
        }
    }

    /// 文本行: 对齐/加粗/倍宽变化时下发, 段间绝对定位(ESC $), 行尾换行
    private void compileText(LineModel.TextLine line) {
        if (!lastAlign.equals(line.align() == null ? "left" : line.align())) {
            lastAlign = line.align() == null ? "left" : line.align();
            write(0x1B, 0x61, alignCode(lastAlign));
        }
        for (LineModel.Segment segment : line.segments()) {
            if (segment.bold() != lastBold) {
                lastBold = segment.bold();
                write(0x1B, 0x45, lastBold ? 1 : 0);
            }
            if (segment.doubleWidth() != lastDoubleWidth) {
                lastDoubleWidth = segment.doubleWidth();
                write(0x1D, 0x21, lastDoubleWidth ? 0x11 : 0x00);
            }
            if (segment.position() != null) {
                int dots = Math.clamp(segment.position(), 0, 0xFFFF);
                write(0x1B, 0x24, dots & 0xFF, dots >> 8);
            }
            // 最后一道兜底: GBK 外字符(半角 ¥/✓/emoji)落纸必乱码, 下发前统一折叠
            out.writeBytes(PrintCharset.sanitize(segment.text()).getBytes(GB));
        }
        write(0x0A);
    }

    /// CODE128 条码(GS k 73, 函数 B 语法: 长度前缀 + 数据)
    ///
    /// 条码按当前对齐落纸, 与 [compileQr] 同口径显式下发居中 —— 否则继承前一行的对齐状态,
    /// 模板里条码常跟在左对齐 kv 行后, 落纸偏左而预览居中, 破坏「预览=落纸」保证。
    private void compileBarcode(LineModel.BarcodeLine line) {
        if (!"center".equals(lastAlign)) {
            lastAlign = "center";
            write(0x1B, 0x61, AlignCode.CENTER.code);
        }
        byte[] data = Code128Encoder.toEscPosData(line.content()).getBytes(GB);
        if (data.length > 255) {
            throw new IllegalArgumentException("条形码内容过长(" + data.length + " 字节, 限 255)");
        }
        // 模块宽下限 1: 合成层几何守卫会为超宽条码降到 1(模板侧声明值已被解析层限 2~6)
        int moduleWidth = clamp(line.moduleWidth(), 1, 6);
        write(0x1D, 0x77, moduleWidth);
        int height = clamp(line.height(), 1, 255);
        write(0x1D, 0x68, height);
        write(0x1D, 0x48, 0x02);
        write(0x1D, 0x66, 0x00);
        write(0x1D, 0x6B, 73, data.length);
        for (byte b : data) {
            out.write(b & 0xFF);
        }
    }

    /// 一行文本(分隔线用, 不改对齐/加粗状态)
    private void textLine(String text) {
        out.writeBytes(PrintCharset.sanitize(text).getBytes(GB));
        write(0x0A);
    }

    /// 二维码: 固定居中后走固件原生指令
    ///
    /// [LineModel.QrLine] 不带对齐字段(QrElement 只有 value), 不能继承前一行的对齐状态;
    /// 原生二维码按当前对齐打印, 故这里显式下发居中 —— 与 APP 自检票同口径,
    /// 也避免模板里二维码紧跟左/右对齐元素时位置漂移。
    private void compileQr(LineModel.QrLine line) {
        if (!"center".equals(lastAlign)) {
            lastAlign = "center";
            write(0x1B, 0x61, AlignCode.CENTER.code);
        }
        compileQrNative(line);
    }

    /// 原生二维码指令(GS ( k, 函数 165/167/169/180/181)
    ///
    /// 数据量与内容等长(20 字节内容整段约 61 字节), 蓝牙 SPP 下传输约 0.06s。
    /// 存数据按 [QR_NATIVE_CHUNK] 分块(m=48 首块覆盖 / m=49 追加): 部分固件符号缓冲仅 256 字节,
    /// 一次塞满会截断; 分块按字符边界切, 避免 GB18030 多字节序列被劈开成乱码。
    private void compileQrNative(LineModel.QrLine line) {
        write(0x1D, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00);
        int moduleDots = clamp(line.moduleDots(), 1, 16);
        write(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, moduleDots);
        write(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x31);
        List<byte[]> chunks = splitByChar(line.content(), QR_NATIVE_CHUNK);
        for (int i = 0; i < chunks.size(); i++) {
            byte[] data = chunks.get(i);
            int total = 3 + data.length;
            write(0x1D, 0x28, 0x6B, total & 0xFF, total >> 8, 0x31, 0x50, i == 0 ? 0x30 : 0x31);
            out.writeBytes(data);
        }
        write(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30);
        write(0x0A);
    }

    /// 按字符边界切分字节(不劈开 GB18030 多字节序列)
    private static List<byte[]> splitByChar(String content, int maxBytes) {
        List<byte[]> chunks = new ArrayList<>();
        ByteArrayOutputStream cur = new ByteArrayOutputStream();
        int i = 0;
        while (i < content.length()) {
            int cp = content.codePointAt(i);
            byte[] bytes = new String(Character.toChars(cp)).getBytes(GB);
            if (cur.size() > 0 && cur.size() + bytes.length > maxBytes) {
                chunks.add(cur.toByteArray());
                cur.reset();
            }
            cur.writeBytes(bytes);
            i += Character.charCount(cp);
        }
        if (cur.size() > 0) {
            chunks.add(cur.toByteArray());
        }
        return chunks;
    }

    private int alignCode(String align) {
        return switch (align) {
            case "center" -> AlignCode.CENTER.code;
            case "right" -> AlignCode.RIGHT.code;
            default -> AlignCode.LEFT.code;
        };
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void write(int... bytes) {
        for (int b : bytes) {
            out.write(b & 0xFF);
        }
    }

    private static Charset runCatching(String preferred, String fallback) {
        try {
            return Charset.forName(preferred);
        } catch (Exception e) {
            return Charset.forName(fallback);
        }
    }
}
