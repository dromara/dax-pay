package cn.daxpay.open.platform.iam.auth.service.email;

import cn.hutool.core.util.StrUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// # 邮件骨架渲染器
///
/// 样式定死的 HTML 骨架 + 槽位注入: 顶部品牌色横条(白字系统名), 之下五槽位均为纯文本,
/// 渲染时非变量文本整体 HTML 转义(换行转 <br>), 占位符 {var} 在转义后回填
/// (账号/邮箱等行内变量加粗, 验证码走大号数字行), 用户文字无法注入标记, 邮件样式不可破坏;
/// 主题在系统名称已配置时加【系统名】前缀(中英文同样式, 作品牌标识)
public final class MailSkeletonRenderer {

    /// 品牌条底色(与平台前端主色同源的 AntD 蓝)
    private static final String BRAND_BAR_COLOR = "#1677ff";

    /// 系统名称未配置时品牌条的回落品牌名
    public static final String DEFAULT_BRAND = "DaxPay";

    /// 占位符形态: {变量名}(字母开头, 字母数字)
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9]*)\\}");

    private MailSkeletonRenderer() {
    }

    /// 渲染邮件主题(纯文本, 系统名称已配置时加【系统名】前缀, 占位符替换为明文值)
    public static String renderSubject(String subject, Map<String, Object> params, String systemName) {
        String rendered = renderPlain(subject, params);
        return StrUtil.isNotBlank(systemName) ? "【" + systemName + "】" + rendered : rendered;
    }

    /// 渲染完整邮件 HTML
    ///
    /// brandName 为品牌条展示名(调用方已完成空值回落, 见 [DEFAULT_BRAND]),
    /// language 决定 html lang 属性与文案语言无关的骨架细节
    public static String render(MailSkeletonEnum skeleton, MailSlots slots, Map<String, Object> params, String brandName,
            MailLanguageEnum language) {
        String langAttr = language == MailLanguageEnum.zh ? "zh-CN" : "en";
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"").append(langAttr).append("\">\n<head><meta charset=\"UTF-8\"></head>\n");
        html.append("<body style=\"margin:0;padding:0;background-color:#f4f6f8;font-family:'Helvetica Neue',Arial,'PingFang SC','Microsoft YaHei',sans-serif;\">\n");
        html.append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f4f6f8;padding:24px 0;\">\n");
        html.append("<tr><td align=\"center\">\n");
        html.append("<table role=\"presentation\" width=\"560\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:8px;overflow:hidden;\">\n");
        // 品牌色横条(白字系统名, bgcolor 属性兜底部分客户端不支持 style 时的展示)
        html.append("<tr><td bgcolor=\"").append(BRAND_BAR_COLOR).append("\" style=\"background-color:").append(BRAND_BAR_COLOR)
                .append(";padding:20px 32px;font-size:17px;font-weight:600;color:#ffffff;\">")
                .append(escape(brandName)).append("</td></tr>\n");
        // 标题
        html.append("<tr><td style=\"padding:28px 32px 0;font-size:17px;font-weight:600;color:#333333;\">")
                .append(escape(slots.title())).append("</td></tr>\n");
        // 正文
        html.append("<tr><td style=\"padding:16px 32px 0;font-size:14px;color:#333333;line-height:1.65;\">")
                .append(renderInline(slots.content(), params)).append("</td></tr>\n");
        // 验证码(仅验证码型骨架): 大号数字, 无底色无色块
        if (skeleton == MailSkeletonEnum.CODE) {
            Object code = params.get(MailTemplateVariable.code.getName());
            html.append("<tr><td style=\"padding:20px 32px;font-size:32px;font-weight:700;letter-spacing:4px;color:#333333;\">")
                    .append(escape(StrUtil.toStringOrNull(code))).append("</td></tr>\n");
        }
        // 提示行(可空): 验证码型上方码块自带底距, 通知型自带顶距
        if (StrUtil.isNotBlank(slots.tip())) {
            String tipPadding = skeleton == MailSkeletonEnum.CODE ? "8px 32px" : "16px 32px 8px";
            html.append("<tr><td style=\"padding:").append(tipPadding).append(";font-size:13px;color:#999999;line-height:1.65;\">")
                    .append(renderInline(slots.tip(), params)).append("</td></tr>\n");
        }
        // 页脚(可空)
        if (StrUtil.isNotBlank(slots.footer())) {
            html.append("<tr><td style=\"padding:24px 32px 28px;border-top:1px solid #eeeeee;font-size:12px;color:#999999;\">")
                    .append(escape(slots.footer())).append("</td></tr>\n");
        }
        html.append("</table>\n</td></tr>\n</table>\n</body>\n</html>");
        return html.toString();
    }

    /// 提取文案中出现的占位符变量名(保存校验用)
    public static Set<String> extractVariables(String text) {
        Set<String> names = new HashSet<>();
        if (StrUtil.isBlank(text)) {
            return names;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    /// 主题内的占位符替换(纯文本, 不做 HTML 处理)
    private static String renderPlain(String text, Map<String, Object> params) {
        if (StrUtil.isBlank(text)) {
            return text;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            sb.append(text, last, matcher.start());
            Object value = params.get(matcher.group(1));
            sb.append(value != null ? value : matcher.group());
            last = matcher.end();
        }
        sb.append(text.substring(last));
        return sb.toString();
    }

    /// 行内渲染: 纯文本段转义(换行转 <br>), 已声明占位符替换为加粗值, 未声明占位符原样保留(渲染兜底)
    private static String renderInline(String text, Map<String, Object> params) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            sb.append(escapeWithBr(text.substring(last, matcher.start())));
            Object value = params.get(matcher.group(1));
            if (value != null) {
                sb.append("<b>").append(escape(StrUtil.toStringOrNull(value))).append("</b>");
            } else {
                sb.append(matcher.group());
            }
            last = matcher.end();
        }
        sb.append(escapeWithBr(text.substring(last)));
        return sb.toString();
    }

    /// HTML 转义 + 换行转 <br>(槽位为纯文本多行输入)
    private static String escapeWithBr(String text) {
        return escape(text).replace("\r\n", "\n").replace("\n", "<br>");
    }

    /// HTML 转义(五个关键字符, 不引 hutool-http 依赖)
    private static String escape(String text) {
        if (StrUtil.isBlank(text)) {
            return StrUtil.EMPTY;
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
