package cn.daxpay.open.platform.capability.sensitiveword.engine;

import cn.hutool.core.util.StrUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.springframework.stereotype.Component;

/// # 敏感词文本规范化（中文主场景）
///
/// 去零宽、全角转半角、繁体转简体；与 UI locale 无关。
@Component
public class SensitiveTextNormalizer {

    /// 零宽/不可见字符
    private static final String ZERO_WIDTH = "[\\u200B-\\u200D\\uFEFF\\u2060\\u00AD]";

    /// 规范化待检文本
    public String normalize(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        String s = text.replaceAll(ZERO_WIDTH, "");
        s = StrUtil.trim(s);
        s = fullWidthToHalf(s);
        // 繁转简，词库约定简体录入
        s = ZhConverterUtil.toSimple(s);
        return s;
    }

    /// 富文本：剥 HTML 标签后再规范化
    public String normalizeHtml(String html) {
        if (StrUtil.isBlank(html)) {
            return "";
        }
        String plain = html.replaceAll("(?is)<script[^>]*>.*?</script>", " ")
                .replaceAll("(?is)<style[^>]*>.*?</style>", " ")
                .replaceAll("(?s)<[^>]+>", " ")
                .replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replaceAll("\\s+", " ");
        return normalize(plain);
    }

    private static String fullWidthToHalf(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == 12288) {
                chars[i] = ' ';
            } else if (c >= 65281 && c <= 65374) {
                chars[i] = (char) (c - 65248);
            }
        }
        return new String(chars);
    }
}

