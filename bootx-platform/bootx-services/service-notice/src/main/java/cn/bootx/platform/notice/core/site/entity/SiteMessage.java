package cn.bootx.platform.notice.core.site.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.notice.core.site.convert.SiteMessageConvert;
import cn.bootx.platform.notice.code.SiteMessageCode;
import cn.bootx.platform.notice.dto.site.SiteMessageDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 站内信
 *
 * @author xxm
 * @since 2021/8/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("notice_site_message")
public class SiteMessage extends MpBaseEntity implements EntityBaseFunction<SiteMessageDto> {

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    @BigField
    private String content;

    /**
     * 接收对象类型 全体/指定用户
     * @see SiteMessageCode#RECEIVE_ALL
     */
    private String receiveType;

    /**
     * 发布状态
     * @see SiteMessageCode#STATE_SENT
     */
    private String sendState;

    /** 发送者id */
    private Long senderId;

    /** 发送者姓名 */
    private String senderName;

    /** 发送时间 */
    private LocalDateTime senderTime;

    /** 撤销时间 */
    private LocalDateTime cancelTime;

    /** 截至有效期 有效超过有效期后全体通知将无法看到 */
    private LocalDate efficientTime;

    @Override
    public SiteMessageDto toDto() {
        return SiteMessageConvert.CONVERT.convert(this);
    }

}
