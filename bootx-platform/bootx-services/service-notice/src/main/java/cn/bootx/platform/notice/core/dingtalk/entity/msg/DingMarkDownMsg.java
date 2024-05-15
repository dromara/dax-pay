package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.MarkdownMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉MarkDown工作通知消息")
public class DingMarkDownMsg implements DingMsg {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String text;

    @Override
    public Msg toDingMsg() {
        return new MarkdownMsg(title, text);
    }

}
