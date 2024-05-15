package cn.bootx.platform.iam.dto.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 在线用户会话信息
 * @author xxm
 * @since 2023/12/4
 */
@Data
@Accessors(chain = true)
@Schema(title = "在线用户会话信息")
public class OnlineUserSessionDto {

    @Schema(description = "会话id")
    private String sessionId;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "uri")
    private String uri;


}
