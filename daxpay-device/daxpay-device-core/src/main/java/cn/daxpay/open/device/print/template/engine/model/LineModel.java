package cn.daxpay.open.device.print.template.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/// # 小票行模型
///
/// 渲染管线的唯一中间产物(单一事实源): 元素经变量替换与宽度测量后合成的逐行模型,
/// 指令编译出口(驱动方言层的 ESC/POS 编译器, 编排层调用)
/// 与前端 CSS 热敏纸预览组件两出口消费同一份数据, 预览保真是构造性保证。
///
/// 宽度测量口径: 全角字符 24 点 / 半角 12 点(>0x7F 判全角), 倍宽翻倍;
/// 58mm 纸 = 384 dots / 80mm 纸 = 576 dots。
///
/// JSON 出参(sealed record 无判别字段): 各行 record 携带 [JsonProperty] 的 `kind` 常量
/// (text/divider/barcode/qrcode/feed/cut)供前端判别。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 无业务改动。
public record LineModel(int paperWidth, int dotsPerLine, List<Line> lines) {

    /// 行类型(封闭)
    public sealed interface Line permits TextLine, DividerLine, BarcodeLine, QrLine, FeedLine, CutLine {}

    /// 文本行: 若干段组成(两栏 kv 行 = label 段 + 绝对定位的 value 段)
    public record TextLine(String align, List<Segment> segments) implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "text";
        }
    }

    /// 文本段: position 非空表示绝对定位点(ESC $, kv 右值用), null=紧接前文
    public record Segment(String text, boolean bold, boolean doubleWidth, Integer position) {}

    /// 分隔线行
    public record DividerLine(String pattern) implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "divider";
        }
    }

    /// 条码行(CODE128, HRI 字符在下方)
    public record BarcodeLine(String content, int moduleWidth, int height) implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "barcode";
        }
    }

    /// 二维码行: 内容 + 每模块点数, 打印端以 GS ( k 原生指令下发(固件负责纠错/静区/绘制)
    public record QrLine(String content, int moduleDots) implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "qrcode";
        }
    }

    /// 走纸行(预览渲染 n 个空行)
    public record FeedLine(int lines) implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "feed";
        }
    }

    /// 切纸行(预览渲染裁切虚线)
    public record CutLine() implements Line {
        @JsonProperty("kind")
        public String kind() {
            return "cut";
        }
    }
}
