package cn.daxpay.open.platform.iam.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// 邮件模板槽位保存参数
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板槽位保存参数")
public class MailTemplateUpsertParam {

    /// 模板编码
    @NotBlank(message = "{validation.field.mailTemplateCode.notBlank}")
    @Schema(description = "模板编码")
    private String templateCode;

    /// 语言(zh/en)
    @NotBlank(message = "{validation.field.mailLanguage.notBlank}")
    @Schema(description = "语言(zh/en)")
    private String language;

    /// 邮件主题
    @NotBlank(message = "{validation.field.mailSubject.notBlank}")
    @Schema(description = "邮件主题")
    private String subject;

    /// 邮件标题
    @NotBlank(message = "{validation.field.mailTitle.notBlank}")
    @Schema(description = "邮件标题")
    private String title;

    /// 正文文案
    @NotBlank(message = "{validation.field.mailContent.notBlank}")
    @Schema(description = "正文文案")
    private String content;

    /// 提示行文案(可空, 空则不输出该行)
    @Schema(description = "提示行文案")
    private String tip;

    /// 页脚文案(可空, 空则不输出该行)
    @Schema(description = "页脚文案")
    private String footer;
}
