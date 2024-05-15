package cn.bootx.platform.starter.dingtalk.param.notice;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钉钉 更新工作通知状态栏
 *
 * @author xxm
 * @since 2022/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉更新工作通知状态栏")
public class UpdateCorpNotice {

    @JsonProperty("agent_id")
    @Schema(description = "发送消息时使用的微应用的AgentID")
    private Long agentId;

    @JsonProperty("task_id")
    @Schema(description = "发送消息时使用的微应用的AgentID")
    private Long taskId;

    @JsonProperty("status_value")
    @Schema(description = "状态栏值")
    private String statusValue;

    @JsonProperty("status_bg")
    @Schema(description = "状态栏背景色，推荐0xFF加六位颜色值")
    private String statusBg;

    public String toParam() {
        return JacksonUtil.toJson(this);
    }

}
