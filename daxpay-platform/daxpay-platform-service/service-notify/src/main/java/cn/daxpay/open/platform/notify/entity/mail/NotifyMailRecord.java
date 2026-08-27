package cn.daxpay.open.platform.notify.entity.mail;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.notify.convert.mail.NotifyMailRecordConvert;
import cn.daxpay.open.platform.notify.result.mail.NotifyMailRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 邮件发送记录
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("notify_mail_record")
public class NotifyMailRecord extends MpBaseEntity implements ToResult<NotifyMailRecordResult> {

    /// 收件邮箱
    private String receiverEmail;

    /// 收件用户ID(关联 iam_user_info, 非用户发送可空)
    private Long receiverUserId;

    /// 邮件主题
    private String subject;

    /// 邮件正文(HTML)
    private String content;

    /// 业务场景(test测试发送/manual手动发送等)
    private String businessType;

    /// 发送状态(sending发送中/success成功/fail失败)
    private String status;

    /// 失败原因
    private String errorMsg;

    /// 重试次数
    private Integer retryCount;

    /// 实际发送时间
    private OffsetDateTime sendTime;

    @Override
    public NotifyMailRecordResult toResult() {
        return NotifyMailRecordConvert.CONVERT.convert(this);
    }
}
