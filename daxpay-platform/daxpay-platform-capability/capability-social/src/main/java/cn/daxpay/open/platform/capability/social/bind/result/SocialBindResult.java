package cn.daxpay.open.platform.capability.social.bind.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 社交账号绑定结果
///
@Data
@Accessors(chain = true)
@Schema(title = "社交账号绑定结果")
public class SocialBindResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "本地用户ID")
    private Long userId;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "平台编码")
    private String source;

    @Schema(description = "平台用户唯一标识")
    private String openId;

    @Schema(description = "平台昵称")
    private String username;

    @Schema(description = "平台头像")
    private String avatar;

    @Schema(description = "绑定时间")
    private OffsetDateTime createTime;
}
