package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钉钉链接消息
 *
 * @author xxm
 * @since 2020/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉链接消息")
public class LinkMsg extends Msg implements Serializable {

    private static final long serialVersionUID = -3094638065840434973L;

    @Schema(description = "钉钉link消息体")
    private DingLink link;

    public LinkMsg() {
        super(DingTalkNoticeCode.MSG_LINK);
    }

    public LinkMsg(String title, String msg, String messageUrl) {
        super(DingTalkNoticeCode.MSG_LINK);
        link = new DingLink(title, msg, messageUrl);
    }

    public LinkMsg(String title, String msg, String messageUrl, String picUrl) {
        super(DingTalkNoticeCode.MSG_LINK);
        link = new DingLink(title, msg, messageUrl, picUrl);
    }

    @Data
    @Accessors(chain = true)
    @Schema(title = "钉钉link")
    @NoArgsConstructor
    public static class DingLink implements Serializable {

        private static final long serialVersionUID = 8191181631664337904L;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "内容")
        private String text;

        @Schema(description = "点击消息跳转的URL")
        private String messageUrl;

        @Schema(description = "图片URL")
        private String picUrl;

        public DingLink(String title, String text, String messageUrl) {
            this.title = title;
            this.text = text;
            this.messageUrl = messageUrl;
        }

        public DingLink(String title, String text, String messageUrl, String picUrl) {
            this.title = title;
            this.text = text;
            this.messageUrl = messageUrl;
            this.picUrl = picUrl;
        }

    }

}
