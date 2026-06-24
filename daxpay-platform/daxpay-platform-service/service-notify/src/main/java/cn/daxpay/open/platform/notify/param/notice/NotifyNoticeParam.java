package cn.daxpay.open.platform.notify.param.notice;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 公告参数(管理端 新建/编辑)
@Data
@Accessors(chain = true)
@Schema(title = "公告参数")
public class NotifyNoticeParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "{validation.field.title.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "标题")
    private String title;

    @NotBlank(message = "{validation.field.content.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "正文(Markdown)")
    private String content;

    @Schema(description = "重要程度(normal普通/important重要)")
    private String severity;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "生效时间(为空则立即生效)")
    private OffsetDateTime effectiveTime;

    @Schema(description = "过期时间(为空则永久有效)")
    private OffsetDateTime expireTime;
}
