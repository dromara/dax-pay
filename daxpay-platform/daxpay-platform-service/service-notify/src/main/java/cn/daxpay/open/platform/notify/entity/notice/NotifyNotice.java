package cn.daxpay.open.platform.notify.entity.notice;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.notify.convert.notice.NotifyNoticeConvert;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 公告通知(广播型)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("notify_notice")
public class NotifyNotice extends MpBaseEntity implements ToResult<NotifyNoticeResult> {

    /// 标题
    private String title;

    /// 正文(Markdown原文)
    private String content;

    /// 重要程度(normal普通/important重要)
    private String severity;

    /// 是否置顶
    @TableField("is_top")
    private Boolean isTop;

    /// 生效时间(为空则立即生效)
    private OffsetDateTime effectiveTime;

    /// 过期时间(为空则永久有效)
    private OffsetDateTime expireTime;

    /// 状态(draft草稿/published发布/offline下线)
    private String status;

    @Override
    public NotifyNoticeResult toResult() {
        return NotifyNoticeConvert.CONVERT.convert(this);
    }
}
