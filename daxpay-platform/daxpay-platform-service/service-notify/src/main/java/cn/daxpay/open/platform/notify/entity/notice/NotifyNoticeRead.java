package cn.daxpay.open.platform.notify.entity.notice;

import cn.daxpay.open.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// 公告已读记录(用户 x 公告)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("notify_notice_read")
public class NotifyNoticeRead extends MpCreateEntity {

    /// 用户ID
    private Long userId;

    /// 公告ID
    private Long noticeId;

    /// 阅读时间
    private OffsetDateTime readTime;

    /// 是否忽略(用户主动隐藏该公告)
    @TableField("is_ignored")
    private Boolean isIgnored;
}
