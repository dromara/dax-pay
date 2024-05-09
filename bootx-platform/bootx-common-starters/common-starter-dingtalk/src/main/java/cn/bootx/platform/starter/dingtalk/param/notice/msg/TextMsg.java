package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/11/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉文本消息")
public class TextMsg extends Msg implements Serializable {

    private static final long serialVersionUID = 8237431306046852539L;

    @Schema(description = "文本消息体")
    private DingText text;

    public TextMsg() {
        super(DingTalkNoticeCode.MSG_TEXT);
    }

    public TextMsg(String msg) {
        super(DingTalkNoticeCode.MSG_TEXT);
        text = new DingText(msg);
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "钉钉文本")
    public static class DingText implements Serializable {

        private static final long serialVersionUID = 3582073816491238620L;

        @Schema(description = "文本")
        private String content;

    }

}
