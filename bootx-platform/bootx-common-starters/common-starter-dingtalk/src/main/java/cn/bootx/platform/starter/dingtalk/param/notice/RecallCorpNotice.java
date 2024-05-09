package cn.bootx.platform.starter.dingtalk.param.notice;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉撤回工作通知
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "钉钉撤回工作通知")
public class RecallCorpNotice {

    @JsonProperty("agent_id")
    @Schema(description = "发送消息时使用的微应用的AgentID")
    private Long agentId;

    @JsonProperty("msg_task_id")
    @Schema(description = "发送消息时钉钉返回的任务ID")
    private Long taskId;

    public String toParam() {
        return JacksonUtil.toJson(this);
    }

}
