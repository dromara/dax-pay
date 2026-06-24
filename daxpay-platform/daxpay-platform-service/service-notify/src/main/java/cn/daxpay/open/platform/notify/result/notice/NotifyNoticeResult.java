package cn.daxpay.open.platform.notify.result.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 公告详情(管理端)
@Data
@Accessors(chain = true)
@Schema(title = "公告详情")
public class NotifyNoticeResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文(Markdown原文)")
    private String content;

    @Schema(description = "重要程度(normal普通/important重要)")
    private String severity;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "生效时间")
    private OffsetDateTime effectiveTime;

    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

    @Schema(description = "状态(draft草稿/published发布/offline下线)")
    private String status;

    @Schema(description = "创建人ID")
    private Long creator;

    @Schema(description = "创建时间")
    private OffsetDateTime createTime;

    @Schema(description = "最后修改时间")
    private OffsetDateTime lastModifiedTime;
}
