package cn.daxpay.open.platform.notify.result.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 通知项(铃铛列表, 公告与个人消息统一结构)
@Data
@Accessors(chain = true)
@Schema(title = "通知项")
public class NotifyNoticeBriefResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "通知类型(notice公告/message个人消息)")
    private String type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容摘要")
    private String message;

    @Schema(description = "重要程度(公告专用)")
    private String severity;

    @Schema(description = "是否置顶(公告专用)")
    private Boolean isTop;

    @Schema(description = "是否已读")
    private Boolean isRead;

    @Schema(description = "正文HTML(公告专用, 服务端Markdown渲染结果, 供前端直接渲染)")
    private String htmlContent;

    @Schema(description = "跳转链接(个人消息专用)")
    private String link;

    @Schema(description = "创建时间")
    private OffsetDateTime createTime;
}
