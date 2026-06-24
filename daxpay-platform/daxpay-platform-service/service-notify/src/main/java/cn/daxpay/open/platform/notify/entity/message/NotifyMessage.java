package cn.daxpay.open.platform.notify.entity.message;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.notify.convert.notice.NotifyNoticeConvert;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeBriefResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// 个人消息(定向通知, 本次预留建表, 暂不接入业务)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("notify_message")
public class NotifyMessage extends MpBaseEntity implements ToResult<NotifyNoticeBriefResult> {

    /// 接收用户ID
    private Long userId;

    /// 标题
    private String title;

    /// 正文内容
    private String content;

    /// 业务来源(预留)
    private String source;

    /// 跳转链接(内部路由或完整http外链)
    private String link;

    /// 跳转附加参数(JSON字符串)
    private String extra;

    /// 是否已读
    @TableField("is_read")
    private Boolean isRead;

    @Override
    public NotifyNoticeBriefResult toResult() {
        return NotifyNoticeConvert.CONVERT.convert(this);
    }
}
