package cn.daxpay.open.platform.notify.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// 邮件发送记录查询(管理端分页)
@Data
@Accessors(chain = true)
@Schema(title = "邮件发送记录查询")
public class NotifyMailRecordQuery {

    /// 收件邮箱(模糊匹配)
    @Schema(description = "收件邮箱")
    private String receiverEmail;

    /// 发送状态
    @Schema(description = "发送状态")
    private String status;

    /// 业务场景
    @Schema(description = "业务场景")
    private String businessType;
}
