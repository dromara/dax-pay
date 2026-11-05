package cn.daxpay.open.device.print.template.engine;

import cn.daxpay.open.device.print.template.engine.model.LineModel;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsContext;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsItem;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.Align;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.AmountElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.BarcodeElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.CutElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.DividerElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.FeedElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.GoodsElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.KvElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.QrElement;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.Size;
import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement.TextElement;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/// # 小票行合成器
///
/// 元素列表(已变量替换) → [LineModel]: 宽度测量(全角 24 / 半角 12 点, 倍宽翻倍)、
/// 超宽折行、两栏 kv 绝对定位(放不下退化为 `label: value`)、条码非 ASCII 降级文本行、
/// 条码 CODE128 超宽降模块宽(仍超退文本)、二维码编码为模块矩阵并按纸宽自适应模块点数、
/// 商品明细两行式展开。
/// 宽度口径与虚拟设备 APP `EscPosBuilder.measureDots` 完全一致, 保证跨端行为一致。
///
/// 所有文本/两栏/品名在合成时先过 [PrintCharset#sanitize]: 小票机只认 GBK 双字节字库,
/// 半角 `¥`、`✓`、emoji 等 GBK 外字符经 GB18030 编码会变四字节乱码(详见该类注释)。
///
/// 商品明细: 品名左对齐 + 小计右缘绝对定位(与 kv 的 value 同一公式), 第二行为「数量 × 单价」;
/// 单条品名按 [GoodsElement#nameLines] 折行, 超出以省略号截断; 非法条目跳过并记 WARN;
/// 单票条数/总行数受全局兜底约束, 截断时追加汇总行。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 异常文案 messageKey 化。
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrintLineComposer {

    /// 全角字符点宽(24×24 汉字字库)
    public static final int WIDE_CHAR_DOTS = 24;

    /// 半角字符点宽(12×24 ASCII 字库)
    public static final int NARROW_CHAR_DOTS = 12;

    /// 58mm 纸每行点数
    public static final int DOTS_58MM = 384;

    /// 80mm 纸每行点数
    public static final int DOTS_80MM = 576;

    /// 商品明细全局条数兜底(元素 maxItems=0 或不设置时的安全阀; 防异常入参撑爆票据与报文)
    public static final int GOODS_MAX_ITEMS_FALLBACK = 200;

    /// 商品明细全局总行数兜底(防长品名折行放大; 与条数兜底配合)
    public static final int GOODS_MAX_LINES_FALLBACK = 400;

    /// 商品明细品名默认最大行数
    private static final int GOODS_DEFAULT_NAME_LINES = 2;

    /// 合成行模型(无商品明细上下文)
    public static LineModel compose(List<PrintTemplateElement> elements, int paperWidth) {
        return compose(elements, paperWidth, PrintGoodsContext.empty());
    }

    /// 合成行模型(paperWidth 58/80 之外的值按 58 处理)
    public static LineModel compose(List<PrintTemplateElement> elements, int paperWidth, PrintGoodsContext goods) {
        int dotsPerLine = dotsForPaper(paperWidth);
        List<LineModel.Line> lines = new ArrayList<>();
        PrintGoodsContext context = goods == null ? PrintGoodsContext.empty() : goods;
        for (PrintTemplateElement element : elements) {
            appendElement(element, dotsPerLine, context, lines);
        }
        return new LineModel(paperWidth, dotsPerLine, lines);
    }

    /// 纸宽(mm) → 每行点数
    public static int dotsForPaper(int paperWidthMm) {
        return paperWidthMm == 80 ? DOTS_80MM : DOTS_58MM;
    }

    /// 文本打印点宽(全角 24/半角 12, 倍宽翻倍)
    public static int measureDots(String text, boolean doubleWidth) {
        int dots = 0;
        for (int i = 0; i < text.length(); i++) {
            dots += text.charAt(i) > 0x7F ? WIDE_CHAR_DOTS : NARROW_CHAR_DOTS;
        }
        return doubleWidth ? dots * 2 : dots;
    }

    private static void appendElement(PrintTemplateElement element, int dotsPerLine,
                                      PrintGoodsContext goods, List<LineModel.Line> lines) {
        switch (element) {
            case TextElement e -> appendText(e.text(), alignName(e.align()), Boolean.TRUE.equals(e.bold()),
                    e.size() == Size.DOUBLE, dotsPerLine, lines);
            case KvElement e -> appendKv(e, dotsPerLine, lines);
            case DividerElement e -> lines.add(new LineModel.DividerLine(e.pattern()));
            // 金额前缀必须用全角「￥」(U+FFE5, GBK A3 A4): 半角 ¥(U+00A5) 不在 GBK 字库,
            // GB18030 会编成四字节 81 30 84 36, 固件丢引导字节后把 0/6 当 ASCII 打出(¥9999.99 → 069999.99)
            case AmountElement e -> appendText("￥" + e.value(), alignName(e.align()),
                    !Boolean.FALSE.equals(e.bold()), e.size() == Size.DOUBLE, dotsPerLine, lines);
            case BarcodeElement e -> appendBarcode(e, dotsPerLine, lines);
            case QrElement e -> appendQr(e, dotsPerLine, lines);
            case GoodsElement e -> appendGoodsSafely(e, dotsPerLine, goods, lines);
            case FeedElement e -> lines.add(new LineModel.FeedLine(e.lines()));
            case CutElement e -> lines.add(new LineModel.CutLine());
        }
    }

    /// 文本行(超宽按字符宽度累积折行, 续行继承对齐与样式)
    ///
    /// 入口先过 [PrintCharset#sanitize]: 折叠半角 `¥` 等 GBK 无码位符号, 保证预览文本
    /// 与实际落纸字节同源(预览看到什么, 纸上就是什么)
    private static void appendText(String text, String align, boolean bold, boolean doubleWidth, int dotsPerLine, List<LineModel.Line> lines) {
        int limit = doubleWidth ? dotsPerLine / 2 : dotsPerLine;
        String remain = PrintCharset.sanitize(text);
        while (true) {
            int take = fitChars(remain, limit);
            String part = remain.substring(0, take);
            lines.add(new LineModel.TextLine(align, List.of(new LineModel.Segment(part, bold, doubleWidth, null))));
            remain = remain.substring(take);
            if (remain.isEmpty()) {
                return;
            }
        }
    }

    /// 两栏行: 放得下走绝对定位, 放不下退化为 `label: value` 普通行(对齐 APP kvFits 口径)
    /// skipIfEmpty=true 且 value 为空白时整行跳过(变量缺项场景, 如差额为 0 的优惠行)
    /// 两栏文本先过 [PrintCharset#sanitize]: 变量(商户名/商品名)可能夹带 GBK 外字符
    private static void appendKv(KvElement e, int dotsPerLine, List<LineModel.Line> lines) {
        boolean bold = Boolean.TRUE.equals(e.bold());
        String label = PrintCharset.sanitize(e.label());
        String value = PrintCharset.sanitize(e.value() == null ? "" : e.value());
        if (Boolean.TRUE.equals(e.skipIfEmpty()) && value.isBlank()) {
            return;
        }
        if (kvFits(label, value, dotsPerLine)) {
            List<LineModel.Segment> segments = new ArrayList<>(2);
            segments.add(new LineModel.Segment(label, bold, false, null));
            segments.add(new LineModel.Segment(value, bold, false, dotsPerLine - measureDots(value, false)));
            lines.add(new LineModel.TextLine(alignName(Align.LEFT), segments));
        } else {
            appendText(label + ": " + value, alignName(Align.LEFT), bold, false, dotsPerLine, lines);
        }
    }

    /// 商品明细块: 异常只丢弃本元素(不触发全局兜底模板替换, 避免一条脏数据换掉整票版式)
    private static void appendGoodsSafely(GoodsElement e, int dotsPerLine,
                                          PrintGoodsContext goods, List<LineModel.Line> lines) {
        try {
            appendGoods(e, dotsPerLine, goods, lines);
        } catch (Exception ex) {
            log.error("商品明细渲染失败, 本次跳过该元素: dotsPerLine={}, showPrice={}, nameLines={}",
                    dotsPerLine, e.showPrice(), e.nameLines(), ex);
        }
    }

    /// 商品明细块(两行式): 首行 品名 + 右对小计, 次行「数量 × 单价」; 超限截断并追加汇总行
    private static void appendGoods(GoodsElement e, int dotsPerLine,
                                    PrintGoodsContext goods, List<LineModel.Line> lines) {
        if (goods == null || goods.isEmpty()) {
            return;
        }
        boolean showPrice = !Boolean.FALSE.equals(e.showPrice());
        int nameLines = e.nameLines() == null ? GOODS_DEFAULT_NAME_LINES : Math.max(1, e.nameLines());
        int maxItems = e.maxItems() == null ? 0 : e.maxItems();
        int itemLimit = maxItems <= 0 ? GOODS_MAX_ITEMS_FALLBACK : Math.min(maxItems, GOODS_MAX_ITEMS_FALLBACK);

        List<PrintGoodsItem> validItems = new ArrayList<>(goods.items().size());
        for (PrintGoodsItem item : goods.items()) {
            if (isValidItem(item)) {
                validItems.add(item);
            } else {
                log.warn("商品明细条目非法, 已跳过: name={}, quantity={}, unitPrice={}",
                        item == null ? null : item.name(),
                        item == null ? null : item.quantity(),
                        item == null ? null : item.unitPrice());
            }
        }
        if (validItems.isEmpty()) {
            return;
        }

        int printed = 0;
        int usedLines = 0;
        for (PrintGoodsItem item : validItems) {
            if (printed >= itemLimit) {
                break;
            }
            List<LineModel.Line> itemLines = buildGoodsItemLines(item, showPrice, nameLines, dotsPerLine);
            if (usedLines + itemLines.size() > GOODS_MAX_LINES_FALLBACK) {
                break;
            }
            lines.addAll(itemLines);
            usedLines += itemLines.size();
            printed++;
        }
        int remain = validItems.size() - printed;
        if (remain > 0) {
            lines.add(new LineModel.TextLine(alignName(Align.LEFT),
                    List.of(new LineModel.Segment("… 其余 " + remain + " 项未打印", false, false, null))));
        }
    }

    /// 单条明细 → 1~2 行(品名折行行 + 数量单价行)
    private static List<LineModel.Line> buildGoodsItemLines(PrintGoodsItem item, boolean showPrice,
                                                            int nameLines, int dotsPerLine) {
        List<LineModel.Line> itemLines = new ArrayList<>(2);
        String subtotal = PrintVars.asAmountYuan(item.subtotal());
        int subtotalDots = measureDots(subtotal, false);
        // 首行品名让位给小计并预留一个半角间隙; 极小纸宽下兜底为 4 个半角, 防负数
        int firstLimit = Math.max(NARROW_CHAR_DOTS * 4, dotsPerLine - subtotalDots - NARROW_CHAR_DOTS);
        List<String> nameParts = splitGoodsName(PrintCharset.sanitize(item.name()), nameLines, firstLimit, dotsPerLine);
        for (int i = 0; i < nameParts.size(); i++) {
            if (i == 0) {
                List<LineModel.Segment> segments = new ArrayList<>(2);
                segments.add(new LineModel.Segment(nameParts.getFirst(), false, false, null));
                // 小计右缘对齐: 与 kv 的 value 段同一公式(dotsPerLine - 文本宽度)
                segments.add(new LineModel.Segment(subtotal, false, false, dotsPerLine - subtotalDots));
                itemLines.add(new LineModel.TextLine(alignName(Align.LEFT), segments));
            } else {
                itemLines.add(new LineModel.TextLine(alignName(Align.LEFT),
                        List.of(new LineModel.Segment(nameParts.get(i), false, false, null))));
            }
        }
        if (showPrice) {
            // 两个半角缩进 + 「数量 × 单价」, 单行输出(最长约 20 字符, 58mm 行宽足够)
            String priceLine = "  " + item.quantity() + " × " + PrintVars.asAmountYuan(item.unitPrice());
            itemLines.add(new LineModel.TextLine(alignName(Align.LEFT),
                    List.of(new LineModel.Segment(priceLine, false, false, null))));
        }
        return itemLines;
    }

    /// 品名按行宽切分: 首行受小计占位限制, 续行吃满整行; 超出最大行数以省略号截断
    private static List<String> splitGoodsName(String name, int maxLines, int firstLimit, int restLimit) {
        List<String> parts = new ArrayList<>(maxLines);
        String remain = name;
        for (int i = 0; i < maxLines && !remain.isEmpty(); i++) {
            int limit = i == 0 ? firstLimit : restLimit;
            if (measureDots(remain, false) <= limit) {
                parts.add(remain);
                return parts;
            }
            if (i == maxLines - 1) {
                // 末行: 预留省略号宽度(全角 24 点)后截断, 至少保留 1 个字符
                int take = fitChars(remain, Math.max(NARROW_CHAR_DOTS, limit - WIDE_CHAR_DOTS));
                parts.add(remain.substring(0, take) + "…");
                return parts;
            }
            int take = fitChars(remain, limit);
            parts.add(remain.substring(0, take));
            remain = remain.substring(take);
        }
        return parts;
    }

    /// 明细条目是否可打印(名称非空, 数量与单价为正)
    private static boolean isValidItem(PrintGoodsItem item) {
        return item != null
                && item.name() != null && !item.name().isBlank()
                && item.quantity() > 0
                && item.unitPrice() > 0;
    }

    /// 条码行: 内容空跳过, 非 ASCII 降级为居中文本行(对齐 APP 行为, 防固件乱码);
    /// CODE128 超宽降模块宽, 仍超退居中文本行
    ///
    /// 几何守卫放在合成层是拍板结论: 降级会改变行结构, 必须在进 [LineModel] 之前,
    /// 否则预览画条码、落纸变文本(或被裁剪不可扫), 破坏「预览=落纸」的构造性保证。
    private static void appendBarcode(BarcodeElement e, int dotsPerLine, List<LineModel.Line> lines) {
        String content = e.value() == null ? "" : e.value();
        if (content.isBlank()) {
            return;
        }
        if (!isAscii(content)) {
            appendText(content, alignName(Align.CENTER), false, false, dotsPerLine, lines);
            return;
        }
        int moduleWidth = e.moduleWidth() == null ? (dotsPerLine >= DOTS_80MM ? 3 : 2) : e.moduleWidth();
        // 默认高度 90 dots ≈ 11mm: 常见票面观感下限, 60 dots(7.4mm)实测偏矮(plus 2026-10-24 调)
        int height = e.height() == null ? 90 : e.height();
        // CODE128 超宽必被裁不可扫, 降模块宽, 仍超退文本
        while (moduleWidth > 1 && barcodeDotsWidth(content, moduleWidth) > dotsPerLine) {
            moduleWidth--;
        }
        if (barcodeDotsWidth(content, moduleWidth) > dotsPerLine) {
            appendText(content, alignName(Align.CENTER), false, false, dotsPerLine, lines);
            return;
        }
        lines.add(new LineModel.BarcodeLine(content, moduleWidth, height));
    }

    /// CODE128 宽度 = 模块数 × 模块宽; 模块数由 [BarcodeCodec] 按输出形态精确计算
    /// (Code C 全串压缩的码只有 11×(n/2)+35 模块, 若按纯 B 的 11n+35 误算会过早压模块宽)
    private static int barcodeDotsWidth(String content, int moduleWidth) {
        return BarcodeCodec.moduleCount(content) * moduleWidth;
    }

    /// 二维码行: 空内容(变量缺项)跳过; 目标宽统一 58mm 物理口径(见 [QrCodec#moduleDots]), 两档纸宽落纸尺寸一致
    private static void appendQr(QrElement e, int dotsPerLine, List<LineModel.Line> lines) {
        String content = e.value() == null ? "" : e.value().trim();
        if (content.isEmpty()) {
            return;
        }
        // 编码一次拿模块数: 顺带校验内容合法性(超长/不可编码在此抛出), 尺寸在合成期定完
        int modules = QrCodec.modules(content);
        int moduleDots = QrCodec.moduleDots(modules, dotsPerLine);
        if (moduleDots < QrCodec.MIN_MODULE_DOTS) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.device.print.qrTooLong", content.length(),
                    dotsPerLine == DOTS_80MM ? 80 : 58, QrCodec.MIN_MODULE_DOTS);
        }
        lines.add(new LineModel.QrLine(content, moduleDots));
    }

    /// 逐字符累积, 返回行宽放得下的字符数(至少 1 个, 防死循环)
    private static int fitChars(String text, int limitDots) {
        int dots = 0;
        for (int i = 0; i < text.length(); i++) {
            int charDots = text.charAt(i) > 0x7F ? WIDE_CHAR_DOTS : NARROW_CHAR_DOTS;
            if (i > 0 && dots + charDots > limitDots) {
                return i;
            }
            dots += charDots;
        }
        return text.length();
    }

    /// 两栏是否放得下(中间至少留一个半角空隙, 对齐 APP kvFits)
    private static boolean kvFits(String label, String value, int dotsPerLine) {
        return dotsPerLine - measureDots(value, false) >= measureDots(label, false) + NARROW_CHAR_DOTS;
    }

    private static boolean isAscii(String text) {
        return text.chars().allMatch(c -> c >= 0x20 && c <= 0x7E);
    }

    private static String alignName(Align align) {
        return align == null ? "left" : align.name().toLowerCase();
    }
}
