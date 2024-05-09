package cn.bootx.platform.notice.core.dingtalk.entity.chat;

import cn.bootx.platform.starter.dingtalk.param.notice.ChatNotice;
import cn.bootx.platform.notice.core.dingtalk.entity.msg.DingMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 企业群消息参数
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "企业群消息参数")
public class DingChatNoticeParam {

    @Schema(description = "群Id")
    private String chatId;

    @Schema(description = "钉钉消息内容")
    private DingMsg dingMsg;

    public ChatNotice toDingChatNotice() {
        return new ChatNotice().setChatId(chatId).setMsg(dingMsg.toDingMsg());
    }

}
