package cn.daxpay.open.platform.system.param.protocol;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户协议版本
///
@Data
@Accessors(chain = true)
@Schema(title = "用户协议版本")
public class UserProtocolVersionParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /// 协议ID
    @NotNull(message = "{validation.field.protocolId.notNull}")
    @Schema(description = "协议ID")
    private Long protocolId;

    /// 语言
    @NotBlank(message = "{validation.field.language.notBlank}")
    @Schema(description = "语言")
    private String language;

    /// 版本标签
    @Schema(description = "版本标签")
    private String versionLabel;

    /// 标题
    @NotBlank(message = "{validation.field.title.notBlank}")
    @Schema(description = "标题")
    private String title;

    /// 内容
    @NotBlank(message = "{validation.field.content.notBlank}")
    @Schema(description = "内容")
    private String content;

    /// 渲染后的HTML
    @Schema(description = "渲染后的HTML")
    private String contentHtml;

    /// 内容格式
    @Schema(description = "内容格式")
    private String contentFormat;

    /// 变更说明
    @Schema(description = "变更说明")
    private String summary;
}
