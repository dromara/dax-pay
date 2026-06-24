package cn.daxpay.open.platform.notify.param.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// 公告查询(管理端分页)
@Data
@Accessors(chain = true)
@Schema(title = "公告查询")
public class NotifyNoticeQuery {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "重要程度")
    private String severity;
}
