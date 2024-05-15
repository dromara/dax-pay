package cn.bootx.platform.starter.dingtalk.param.notice;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉发送企业群消息参数
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "钉钉发送企业群消息参数")
public class ChatNotice {

    @JsonProperty("chatid")
    @Schema(description = "群Id")
    private String chatId;

    @Schema(description = "消息内容")
    private Msg msg;

    /**
     * 输出参数
     */
    public String toParam() {
        return JacksonUtil.toJson(this);
    }

}
