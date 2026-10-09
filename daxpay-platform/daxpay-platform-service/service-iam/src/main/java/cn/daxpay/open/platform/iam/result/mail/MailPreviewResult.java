package cn.daxpay.open.platform.iam.result.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// 邮件模板预览渲染结果
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板预览渲染结果")
public class MailPreviewResult {

    /// 渲染后的邮件主题(纯文本)
    @Schema(description = "渲染后的邮件主题")
    private String subject;

    /// 渲染后的邮件正文(完整 HTML, 样式定死)
    @Schema(description = "渲染后的邮件正文")
    private String html;
}
