package cn.daxpay.open.device.print.template.engine;

import cn.daxpay.open.device.print.template.engine.model.PrintTemplateElement;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// # 打印模板解析器
///
/// 模板 content JSON → 元素列表: 手动按 type 分发装配(JsonNode 显式读取, 未知属性容忍),
/// 并完成结构校验(元素类型/枚举/长度/范围/占位符白名单), 校验失败抛 [BizInfoException]
/// (messageKey 定位到元素下标)。规范化([normalize])输出 `{"elements":[...]}` 根对象, 剥离模板级冗余属性。
///
/// 开源版模板来源为代码内置常量([cn.daxpay.open.device.print.template.PrintTemplateConstants]),
/// 本解析器在正常链路不会抛错(常量内容随代码评审把关); 校验逻辑完整保留,
/// 供后续扩展自定义版式时复用。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 异常文案 messageKey 化。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrintTemplateParser {

    /// 占位符语法: ${name}
    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([A-Za-z0-9_]+)}");

    /// 文本类属性长度上限
    private static final int MAX_TEXT_LENGTH = 200;

    /// 元素数上限
    private static final int MAX_ELEMENTS = 100;

    /// 商品明细品名默认最大行数
    private static final int DEFAULT_GOODS_NAME_LINES = 2;

    /// 商品明细默认条数上限(0=全打)
    private static final int DEFAULT_GOODS_MAX_ITEMS = 0;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /// 解析并校验模板 content(根对象含 elements 数组, 或裸数组)
    public static List<PrintTemplateElement> parse(String content) {
        JsonNode elementsNode = readElementsNode(content);
        if (!elementsNode.isArray() || elementsNode.isEmpty()) {
            throw error("error.device.print.elementsEmpty");
        }
        if (elementsNode.size() > MAX_ELEMENTS) {
            throw error("error.device.print.elementLimit", MAX_ELEMENTS);
        }
        List<PrintTemplateElement> elements = new ArrayList<>(elementsNode.size());
        for (int i = 0; i < elementsNode.size(); i++) {
            elements.add(parseElement(elementsNode.get(i), i));
        }
        return elements;
    }

    /// 规范化模板 content: 保留 elements 原始节点(属性顺序与未知属性不丢失), 剥离根级冗余属性
    public static String normalize(String content) {
        JsonNode elementsNode = readElementsNode(content);
        ObjectNode root = MAPPER.createObjectNode();
        root.set("elements", elementsNode);
        return root.toString();
    }

    /// 变量替换: 文本类字段中 `${name}` → vars 取值(缺项替换为空串), 返回替换后的新元素列表
    public static List<PrintTemplateElement> resolve(List<PrintTemplateElement> elements, Map<String, String> vars) {
        List<PrintTemplateElement> resolved = new ArrayList<>(elements.size());
        for (PrintTemplateElement element : elements) {
            resolved.add(switch (element) {
                case PrintTemplateElement.TextElement e -> new PrintTemplateElement.TextElement(
                        substitute(e.text(), vars), e.align(), e.bold(), e.size());
                case PrintTemplateElement.KvElement e -> new PrintTemplateElement.KvElement(
                        substitute(e.label(), vars), substitute(e.value(), vars), e.bold(), e.skipIfEmpty());
                case PrintTemplateElement.AmountElement e -> new PrintTemplateElement.AmountElement(
                        substitute(e.value(), vars), e.align(), e.bold(), e.size());
                case PrintTemplateElement.BarcodeElement e -> new PrintTemplateElement.BarcodeElement(
                        substitute(e.value(), vars), e.height(), e.moduleWidth());
                case PrintTemplateElement.QrElement e -> new PrintTemplateElement.QrElement(
                        substitute(e.value(), vars));
                // 商品明细消费独立上下文, 不走占位符替换
                case PrintTemplateElement.GoodsElement e -> e;
                case PrintTemplateElement.DividerElement e -> e;
                case PrintTemplateElement.FeedElement e -> e;
                case PrintTemplateElement.CutElement e -> e;
            });
        }
        return resolved;
    }

    /// 提取文本中的全部占位符变量名(校验用)
    static List<String> placeholderNames(String text) {
        List<String> names = new ArrayList<>();
        Matcher matcher = PLACEHOLDER.matcher(text);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    /// 读取根 elements 节点(根对象取 elements 属性, 裸数组直接用)
    private static JsonNode readElementsNode(String content) {
        if (content == null || content.isBlank()) {
            throw error("error.device.print.contentBlank");
        }
        JsonNode root;
        try {
            root = MAPPER.readTree(content);
        } catch (Exception e) {
            throw error("error.device.print.notJson");
        }
        if (root.isArray()) {
            return root;
        }
        if (root.isObject()) {
            JsonNode elements = root.get("elements");
            if (elements != null && elements.isArray()) {
                return elements;
            }
        }
        throw error("error.device.print.rootInvalid");
    }

    /// 单元素装配 + 校验(报错定位元素下标)
    private static PrintTemplateElement parseElement(JsonNode node, int index) {
        if (node == null || !node.isObject()) {
            throw error("error.device.print.elementNotObject", index + 1);
        }
        String type = text(node, "type");
        return switch (type == null ? "" : type) {
            case "text" -> parseText(node, index);
            case "kv" -> parseKv(node, index);
            case "divider" -> parseDivider(node, index);
            case "amount" -> parseAmount(node, index);
            case "barcode" -> parseBarcode(node, index);
            case "qrcode" -> parseQrcode(node, index);
            case "goods" -> parseGoods(node, index);
            case "feed" -> parseFeed(node, index);
            case "cut" -> new PrintTemplateElement.CutElement();
            default -> throw error("error.device.print.unknownType", index + 1, type == null ? "(missing)" : type);
        };
    }

    private static PrintTemplateElement parseText(JsonNode node, int index) {
        String textValue = requiredText(node, "text", index, "text");
        validatePlaceholders(textValue, index, "text");
        return new PrintTemplateElement.TextElement(textValue, parseAlign(node, index, PrintTemplateElement.Align.LEFT),
                parseBool(node, "bold", false), parseSize(node, index, PrintTemplateElement.Size.NORMAL));
    }

    private static PrintTemplateElement parseKv(JsonNode node, int index) {
        String label = requiredText(node, "label", index, "kv");
        String value = text(node, "value");
        if (value != null && value.length() > MAX_TEXT_LENGTH) {
            throw fieldError(index, "kv", "value", "error.device.print.fieldOverLength", MAX_TEXT_LENGTH);
        }
        validatePlaceholders(label, index, "kv");
        if (value != null) {
            validatePlaceholders(value, index, "kv");
        }
        return new PrintTemplateElement.KvElement(label, value == null ? "" : value,
                parseBool(node, "bold", false), parseBool(node, "skipIfEmpty", false));
    }

    private static PrintTemplateElement parseDivider(JsonNode node, int index) {
        String pattern = text(node, "char");
        if (pattern == null || pattern.isEmpty()) {
            return new PrintTemplateElement.DividerElement("-");
        }
        if (pattern.length() != 1 || pattern.charAt(0) > 0x7E) {
            throw error("error.device.print.dividerInvalid", index + 1);
        }
        return new PrintTemplateElement.DividerElement(pattern);
    }

    private static PrintTemplateElement parseAmount(JsonNode node, int index) {
        String value = text(node, "value");
        if (value == null || value.isEmpty()) {
            value = "${" + PrintVars.AMOUNT_YUAN + "}";
        }
        if (value.length() > MAX_TEXT_LENGTH) {
            throw fieldError(index, "amount", "value", "error.device.print.fieldOverLength", MAX_TEXT_LENGTH);
        }
        validatePlaceholders(value, index, "amount");
        return new PrintTemplateElement.AmountElement(value, parseAlign(node, index, PrintTemplateElement.Align.RIGHT),
                parseBool(node, "bold", true), parseSize(node, index, PrintTemplateElement.Size.DOUBLE));
    }

    private static PrintTemplateElement parseBarcode(JsonNode node, int index) {
        String value = requiredText(node, "value", index, "barcode");
        if (value.length() > MAX_TEXT_LENGTH) {
            throw fieldError(index, "barcode", "value", "error.device.print.fieldOverLength", MAX_TEXT_LENGTH);
        }
        validatePlaceholders(value, index, "barcode");
        Integer height = intInRange(node, "height", 30, 240, 60, index, "barcode");
        Integer moduleWidth = intInRange(node, "moduleWidth", 2, 6, null, index, "barcode");
        return new PrintTemplateElement.BarcodeElement(value, height, moduleWidth);
    }

    /// 二维码: 内容可为单号或链接(支持变量); 长度上限与文本一致, 密度门槛在合成阶段按 58mm 最坏纸宽校验
    private static PrintTemplateElement parseQrcode(JsonNode node, int index) {
        String value = requiredText(node, "value", index, "qrcode");
        validatePlaceholders(value, index, "qrcode");
        return new PrintTemplateElement.QrElement(value);
    }

    /// 商品明细: 属性均不参与变量替换(消费独立明细上下文), 未知属性容忍
    private static PrintTemplateElement parseGoods(JsonNode node, int index) {
        boolean showPrice = parseBool(node, "showPrice", true);
        Integer nameLines = intInRange(node, "nameLines", 1, 4, DEFAULT_GOODS_NAME_LINES, index, "goods");
        Integer maxItems = intInRange(node, "maxItems", 0, 999, DEFAULT_GOODS_MAX_ITEMS, index, "goods");
        return new PrintTemplateElement.GoodsElement(showPrice, nameLines, maxItems);
    }

    private static PrintTemplateElement parseFeed(JsonNode node, int index) {
        Integer lines = intInRange(node, "lines", 0, 255, 1, index, "feed");
        return new PrintTemplateElement.FeedElement(lines);
    }

    /// 占位符白名单校验(未知变量报错定位元素)
    private static void validatePlaceholders(String text, int index, String type) {
        for (String name : placeholderNames(text)) {
            if (!PrintVars.NAMES.contains(name)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.device.print.unknownVar", index + 1, type, name);
            }
        }
    }

    private static PrintTemplateElement.Align parseAlign(JsonNode node, int index, PrintTemplateElement.Align defaultValue) {
        String align = text(node, "align");
        if (align == null || align.isEmpty()) {
            return defaultValue;
        }
        try {
            return PrintTemplateElement.Align.valueOf(align.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw error("error.device.print.alignInvalid", index + 1);
        }
    }

    private static PrintTemplateElement.Size parseSize(JsonNode node, int index, PrintTemplateElement.Size defaultValue) {
        String size = text(node, "size");
        if (size == null || size.isEmpty()) {
            return defaultValue;
        }
        try {
            return PrintTemplateElement.Size.valueOf(size.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw error("error.device.print.sizeInvalid", index + 1);
        }
    }

    private static boolean parseBool(JsonNode node, String field, boolean defaultValue) {
        JsonNode v = node.get(field);
        return v == null || !v.isBoolean() ? defaultValue : v.asBoolean();
    }

    private static String text(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return v == null || v.isNull() ? null : v.asText();
    }

    private static String requiredText(JsonNode node, String field, int index, String type) {
        String value = text(node, field);
        if (value == null || value.isBlank()) {
            throw fieldError(index, type, field, "error.device.print.fieldRequired");
        }
        if (value.length() > MAX_TEXT_LENGTH) {
            throw fieldError(index, type, field, "error.device.print.fieldOverLength", MAX_TEXT_LENGTH);
        }
        return value;
    }

    private static Integer intInRange(JsonNode node, String field, int min, int max, Integer defaultValue, int index, String type) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            return defaultValue;
        }
        if (!v.isInt()) {
            throw fieldError(index, type, field, "error.device.print.fieldNotInt");
        }
        int value = v.asInt();
        if (value < min || value > max) {
            throw fieldError(index, type, field, "error.device.print.fieldRange", min, max);
        }
        return value;
    }

    private static String substitute(String text, Map<String, String> vars) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        Matcher matcher = PLACEHOLDER.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String value = vars.get(matcher.group(1));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value == null ? "" : value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static BizInfoException error(String messageKey, Object... args) {
        return new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, messageKey, args);
    }

    /// 字段级报错(定位元素下标/类型/字段名)
    private static BizInfoException fieldError(int index, String type, String field, String messageKey, Object... args) {
        Object[] merged = new Object[args.length + 3];
        merged[0] = index + 1;
        merged[1] = type;
        merged[2] = field;
        System.arraycopy(args, 0, merged, 3, args.length);
        return new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, messageKey, merged);
    }
}
