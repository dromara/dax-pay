package cn.bootx.platform.notice.core.site.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息用户关联信息
 *
 * @author xxm
 * @since 2022/8/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("notice_site_message_user")
public class SiteMessageUser extends MpCreateEntity {

    /** 消息id */
    private Long messageId;

    /** 接收者id */
    private Long receiveId;

    /** 已读/未读 */
    private boolean haveRead;

    /** 已读时间 */
    private LocalDateTime readTime;

}
