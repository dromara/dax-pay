package cn.bootx.platform.starter.dingtalk.core.notice.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钉钉消息响应
 *
 * @author xxm
 * @since 2022/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "通知消息返回值")
public class CorpNoticeResult {

    @JsonProperty("errcode")
    @Schema(description = "错误码")
    private Integer code;

    @JsonProperty("errmsg")
    @Schema(description = "返回码描述")
    private String msg;

    @JsonProperty("task_id")
    @Schema(description = "异步发送任务ID")
    private Long taskId;

    @JsonProperty("request_id")
    @Schema(description = "请求ID")
    private String requestId;

}
