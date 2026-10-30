package cn.daxpay.open.device.print.template.engine.model;

import java.util.List;

/// # 打印模板元素
///
/// 小票模板的元素模型(共 9 类, 线性流式排版): JSON 由
/// [cn.daxpay.open.device.print.template.engine.PrintTemplateParser] 手动解析装配
/// (未知属性容忍, 未知 type 报错定位元素下标)。
/// 二维码(qrcode)走 ZXing 校验与尺寸推导, 打印由固件 `GS ( k` 原生指令生成。
/// 商品明细(goods)只负责把明细数组展开为行, 表头/合计/实付由模板其他元素表达(整票模板驱动)。
///
/// 对齐与字号: [Align]/[Size] 为封闭枚举, 渲染语义见 [cn.daxpay.open.device.print.template.engine.PrintLineComposer]
/// 与驱动方言层的 ESC/POS 编译器。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public sealed interface PrintTemplateElement
        permits PrintTemplateElement.TextElement, PrintTemplateElement.KvElement,
        PrintTemplateElement.DividerElement, PrintTemplateElement.AmountElement,
        PrintTemplateElement.BarcodeElement, PrintTemplateElement.QrElement,
        PrintTemplateElement.GoodsElement, PrintTemplateElement.FeedElement,
        PrintTemplateElement.CutElement {

    /// 对齐方式(ESC a)
    enum Align {LEFT, CENTER, RIGHT}

    /// 字号(normal=标准, double=倍宽倍高 GS !)
    enum Size {NORMAL, DOUBLE}

    /// 文本行: 一行文本, 支持对齐/加粗/倍宽倍高
    record TextElement(String text, Align align, Boolean bold, Size size) implements PrintTemplateElement {}

    /// 两栏行: label 左、value 右缘对齐(ESC $ 绝对定位), 放不下退化为 `label: value` 普通行
    /// skipIfEmpty=true 时 value(变量替换后)为空则整行跳过(默认 false)
    record KvElement(String label, String value, Boolean bold, Boolean skipIfEmpty) implements PrintTemplateElement {}

    /// 分隔线: 半角字符填满整行(默认 `-`)
    record DividerElement(String pattern) implements PrintTemplateElement {}

    /// 金额行: 金额语义糖, 渲染自动加 `￥` 前缀(value 可选, 默认取 ${amountYuan})
    record AmountElement(String value, Align align, Boolean bold, Size size) implements PrintTemplateElement {}

    /// 条码行: CODE128(内容须 ASCII, 非 ASCII 运行时降级为文本行)
    record BarcodeElement(String value, Integer height, Integer moduleWidth) implements PrintTemplateElement {}

    /// 二维码行: 内容可为单号或链接(支持变量), 打印端按 58mm 最坏纸宽自适应模块点数
    record QrElement(String value) implements PrintTemplateElement {}

    /// 商品明细块: 消费独立的明细上下文(不参与 ${} 变量替换), 两行式展开
    /// showPrice=是否输出「数量 × 单价」行(默认 true); nameLines=品名最多折几行(1~4, 默认 2);
    /// maxItems=最多打几条(0=全打, 默认 0)。表头/合计/实付由模板其他元素负责, 本元素不自动生成。
    record GoodsElement(Boolean showPrice, Integer nameLines, Integer maxItems) implements PrintTemplateElement {}

    /// 走纸 n 行
    record FeedElement(Integer lines) implements PrintTemplateElement {}

    /// 切纸(无切刀机型固件忽略)
    record CutElement() implements PrintTemplateElement {}

    /// 元素在模板中的显示摘要(列表/校验报错定位用)
    static String describe(PrintTemplateElement element) {
        return switch (element) {
            case TextElement e -> "text";
            case KvElement e -> "kv";
            case DividerElement e -> "divider";
            case AmountElement e -> "amount";
            case BarcodeElement e -> "barcode";
            case QrElement e -> "qrcode";
            case GoodsElement e -> "goods";
            case FeedElement e -> "feed";
            case CutElement e -> "cut";
        };
    }

    /// 全部元素类型名(校验提示用)
    List<String> ALL_TYPES = List.of("text", "kv", "divider", "amount", "barcode", "qrcode", "goods", "feed", "cut");
}
