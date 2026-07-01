package cn.daxpay.open.platform.notify.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

/// Markdown 渲染工具类
///
/// 基于 commonmark-java 在服务端将公告 Markdown 正文转换为 HTML 片段,
/// 供前端 mp-html 直接渲染. 这样做的目的:
/// 1. 规避各端(App/小程序/H5) JS 引擎对 ES2018 Unicode 属性转义支持差异导致的白屏;
/// 2. 保证多端渲染结果完全一致.
///
/// Parser 与 HtmlRenderer 均为线程安全, 使用静态单例.
public final class MarkdownRenderUtil {

    private static final List<Extension> EXTENSIONS = List.of(
        TablesExtension.create(),
        StrikethroughExtension.create()
    );

    private static final Parser PARSER = Parser.builder()
        .extensions(EXTENSIONS)
        .build();

    private static final HtmlRenderer RENDERER = HtmlRenderer.builder()
        .extensions(EXTENSIONS)
        .build();

    private MarkdownRenderUtil() {
    }

    /// 将 Markdown 文本渲染为 HTML 片段, null 或空白返回空串
    public static String toHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        return RENDERER.render(PARSER.parse(markdown));
    }
}
