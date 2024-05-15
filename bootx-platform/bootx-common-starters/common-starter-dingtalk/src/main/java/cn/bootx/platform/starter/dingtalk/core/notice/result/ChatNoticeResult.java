package cn.bootx.platform.starter.dingtalk.core.notice.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "通知消息返回值")
public class ChatNoticeResult {

    @JsonProperty("errcode")
    @Schema(description = "错误码")
    private Integer code;

    @JsonProperty("errmsg")
    @Schema(description = "返回码描述")
    private String msg;

    @Schema(description = "异步发送任务ID")
    private String messageId;

}
