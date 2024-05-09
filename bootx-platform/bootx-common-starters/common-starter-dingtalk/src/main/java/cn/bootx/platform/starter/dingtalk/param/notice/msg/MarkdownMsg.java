package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 钉钉markdown消息
 *
 * @author xxm
 * @since 2020/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉文本消息")
public class MarkdownMsg extends Msg implements Serializable {

    private static final long serialVersionUID = -2724590259000709240L;

    @Schema(description = "markdown消息体")
    private DingMarkdown markdown;

    public MarkdownMsg() {
        super(DingTalkNoticeCode.MSG_MARKDOWN);
    }

    /**
     * @param title 标题
     * @param msg 内容
     * @param phones @谁
     */
    public MarkdownMsg(String title, String msg, List<String> phones) {
        super(new At(phones), DingTalkNoticeCode.MSG_MARKDOWN);
        this.markdown = new DingMarkdown(title, msg);
    }

    public MarkdownMsg(String title, String msg) {
        super(DingTalkNoticeCode.MSG_MARKDOWN);
        this.markdown = new DingMarkdown(title, msg);
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @Schema(title = "钉钉markdown格式信息")
    public static class DingMarkdown implements Serializable {

        private static final long serialVersionUID = 2353543901449818541L;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "内容")
        private String text;

        public DingMarkdown(String title, String text) {
            this.title = title;
            this.text = text;
        }

    }

}
