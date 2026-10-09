package cn.daxpay.open.platform.iam.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// 邮件模板预览参数
///
/// 槽位可空, 空槽位以当前生效值(库覆盖或出厂默认)补齐, 供编辑过程中即时预览
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板预览参数")
public class MailTemplatePreviewParam {

    /// 模板编码
    @NotBlank(message = "{validation.field.mailTemplateCode.notBlank}")
    @Schema(description = "模板编码")
    private String templateCode;

    /// 语言(zh/en)
    @NotBlank(message = "{validation.field.mailLanguage.notBlank}")
    @Schema(description = "语言(zh/en)")
    private String language;

    /// 邮件主题(空以生效值补齐)
    @Schema(description = "邮件主题")
    private String subject;

    /// 邮件标题(空以生效值补齐)
    @Schema(description = "邮件标题")
    private String title;

    /// 正文文案(空以生效值补齐)
    @Schema(description = "正文文案")
    private String content;

    /// 提示行文案(空以生效值补齐)
    @Schema(description = "提示行文案")
    private String tip;

    /// 页脚文案(空以生效值补齐)
    @Schema(description = "页脚文案")
    private String footer;
}
