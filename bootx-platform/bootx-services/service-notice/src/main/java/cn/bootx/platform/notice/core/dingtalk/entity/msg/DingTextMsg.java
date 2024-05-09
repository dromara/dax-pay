package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.TextMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉工作通知消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "钉钉文本工作通知消息")
public class DingTextMsg implements DingMsg {

    @Schema(description = "文本内容")
    private String content;

    /**
     * 转换成钉钉消息
     */
    public Msg toDingMsg() {
        return new TextMsg(content);
    }

}
