package cn.daxpay.open.platform.notify.result.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 邮件发送记录
@Data
@Accessors(chain = true)
@Schema(title = "邮件发送记录")
public class NotifyMailRecordResult {

    /// 主键
    @Schema(description = "主键")
    private Long id;

    /// 收件邮箱
    @Schema(description = "收件邮箱")
    private String receiverEmail;

    /// 收件用户ID
    @Schema(description = "收件用户ID")
    private Long receiverUserId;

    /// 邮件主题
    @Schema(description = "邮件主题")
    private String subject;

    /// 邮件正文(HTML)
    @Schema(description = "邮件正文(HTML)")
    private String content;

    /// 业务场景
    @Schema(description = "业务场景")
    private String businessType;

    /// 发送状态
    @Schema(description = "发送状态")
    private String status;

    /// 失败原因
    @Schema(description = "失败原因")
    private String errorMsg;

    /// 重试次数
    @Schema(description = "重试次数")
    private Integer retryCount;

    /// 实际发送时间
    @Schema(description = "实际发送时间")
    private OffsetDateTime sendTime;

    /// 创建时间
    @Schema(description = "创建时间")
    private OffsetDateTime createTime;
}
