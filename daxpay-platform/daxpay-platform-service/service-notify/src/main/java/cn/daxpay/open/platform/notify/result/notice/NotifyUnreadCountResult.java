package cn.daxpay.open.platform.notify.result.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// 未读数
@Data
@Accessors(chain = true)
@Schema(title = "未读数")
public class NotifyUnreadCountResult {

    @Schema(description = "公告未读数")
    private Integer noticeCount;

    @Schema(description = "个人消息未读数")
    private Integer messageCount;

    @Schema(description = "合计未读数")
    private Integer total;
}
