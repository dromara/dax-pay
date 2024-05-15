package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钉钉消息
 *
 * @author xxm
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(title = "钉钉消息基础类")
public class Msg implements Serializable {

    private static final long serialVersionUID = -8548175773944126488L;

    /**
     * <a href="https://open.dingtalk.com/document/group/custom-robot-access">...</a>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "@谁(机器人消息用)")
    private At at;

    /**
     * @see DingTalkNoticeCode
     */
    @JsonProperty("msgtype")
    @Schema(description = "消息类型")
    private String msgType;

    public Msg(At at, String msgType) {
        this.at = at;
        this.msgType = msgType;
    }

    public Msg(String msgType) {
        this.msgType = msgType;
    }

}
