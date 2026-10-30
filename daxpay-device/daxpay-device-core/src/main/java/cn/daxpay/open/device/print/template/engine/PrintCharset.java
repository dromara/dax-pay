package cn.daxpay.open.device.print.template.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.util.Map;

/// # 打印字符集可打印性(GBK 双字节口径)
///
/// 热敏小票机的内置汉字字库是 **GBK 双字节表**, 不认 GB18030 的四字节扩展区。
/// 半角 `¥`(U+00A5)、`✓`(U+2713)、emoji 这类 GBK 无码位的字符, 经 GB18030 编码会产出
/// `81 30 84 36` 形态的四字节序列; 固件丢弃引导字节 `0x81/0x84` 后, 把其中恰好落在
/// `0x30–0x39` 的尾字节当 ASCII 打出来 —— `¥9999.99` 于是落纸成 `069999.99`。
///
/// 两道防线(开源版无模板编辑器, 仅合成兜底一道在生产路径上):
/// - 合成阶段 [sanitize] 兜底替换: 常见符号折叠为 GBK 等价字符(一律单字符换单字符,
///   不改变点宽, 不破坏 kv 右缘绝对定位口径), 其余不可打印字符替换为 `?`;
/// - 模板保存阶段拦截(plus 有, 开源版模板为代码常量不走保存链路, 不移植)。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrintCharset {

    /// 打印机汉字字库编码(GBK 双字节; 仅用于可打印性探测, 实际下发仍走 GB18030)
    private static final Charset GBK = Charset.forName("GBK");

    /// 不可折叠字符的兜底替换符(可打印, 不会干扰行宽)
    public static final char PLACEHOLDER = '?';

    /// 常见 GBK 无码位符号 → GBK 等价字符(值必须单字符, 保证替换前后点宽一致)
    private static final Map<Integer, Character> ALIASES = Map.of(
            0x00A5, '￥', // 半角 ¥ → 全角 ￥(GBK A3 A4, 单字 24 点)
            0x2713, '√', // ✓
            0x2714, '√', // ✔
            0x2717, '×', // ✗
            0x2718, '×'  // ✘
    );

    /// 单码位是否可被 GBK 双字节字库打印(ASCII 占 1 字节, 汉字占 2 字节)
    public static boolean isPrintable(int codePoint) {
        if (codePoint < 0x80) {
            return true;
        }
        if (!Character.isBmpCodePoint(codePoint) || Character.isSurrogate((char) codePoint)) {
            return false;
        }
        // GBK 编不出来的字符会被替换成单个 '?'(1 字节), 编得出来的一律双字节
        return String.valueOf((char) codePoint).getBytes(GBK).length == 2;
    }

    /// 编码前兜底: 常见符号折叠为 GBK 等价字符, 其余不可打印字符替换为 [?]
    ///
    /// 全可打印时原样返回同一个实例(零分配), 热路径无额外开销。
    public static String sanitize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        // 先定位首个需要替换的位置: 之前的内容原样拷贝, 之后逐码位改写
        int firstChanged = -1;
        for (int i = 0; i < text.length(); ) {
            int codePoint = text.codePointAt(i);
            if (ALIASES.containsKey(codePoint) || !isPrintable(codePoint)) {
                firstChanged = i;
                break;
            }
            i += Character.charCount(codePoint);
        }
        if (firstChanged < 0) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length()).append(text, 0, firstChanged);
        for (int i = firstChanged; i < text.length(); ) {
            int codePoint = text.codePointAt(i);
            Character alias = ALIASES.get(codePoint);
            if (alias != null) {
                sb.append(alias.charValue());
            } else if (isPrintable(codePoint)) {
                sb.appendCodePoint(codePoint);
            } else {
                sb.append(PLACEHOLDER);
            }
            i += Character.charCount(codePoint);
        }
        return sb.toString();
    }

    /// 首个既不可打印、也无法折叠的码位(常量内容自检用); 全部可打印返回 -1
    ///
    /// 可折叠符号(如半角 `¥`)不算违规: 合成阶段会自动折叠为全角 `￥`。
    public static int firstUnprintable(String text) {
        if (text == null || text.isEmpty()) {
            return -1;
        }
        return text.codePoints()
                .filter(codePoint -> !ALIASES.containsKey(codePoint) && !isPrintable(codePoint))
                .findFirst()
                .orElse(-1);
    }
}
