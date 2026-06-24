package cn.daxpay.open.platform.notify.param.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// 用户端通知查询(铃铛列表)
@Data
@Accessors(chain = true)
@Schema(title = "用户端通知查询")
public class NotifyUserNoticeQuery {

    @Schema(description = "是否只看未读")
    private Boolean onlyUnread;
}
