package cn.bootx.platform.notice.dto.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 发邮件的参数
 *
 * @author xxm
 * @since 2020/5/2 20:31
 */
@Schema(title = "发邮件的参数")
@Data
@Accessors(chain = true)
public class SendMailParam implements Serializable {

    private static final long serialVersionUID = 7472189938984313186L;

    @Schema(description = "配置编号")
    private String configCode;

    @Schema(description = "标题")
    @NotNull(message = "主题不能为空")
    private String subject;

    @Schema(description = "消息")
    @NotNull(message = "消息不能为空")
    private String message;

    @Schema(description = "接收人")
    @NotNull(message = "接收人不能为空")
    private List<String> to;

    @Schema(description = "抄送人列表")
    private List<String> ccList;

    @Schema(description = "密送")
    private List<String> bccList;

    @Schema(description = "是否单条发送(拆分收件人)  默认true")
    private Boolean singleSend = true;

    @Schema(description = "是否包含附件")
    private boolean sendAttachment;

    @Schema(description = "附件列表")
    private List<MailFileParam> fileList;

}
