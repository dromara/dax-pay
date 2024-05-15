package cn.bootx.platform.notice.event.sms;

import cn.bootx.platform.notice.dto.sms.SmsChannelConfigDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 短信渠道更新事件
 * @author xxm
 * @since 2023/8/5
 */
@Getter
public class SmsChannelUpdateEvent extends ApplicationEvent {
    private final SmsChannelConfigDto channelConfig;

    public SmsChannelUpdateEvent(Object source, SmsChannelConfigDto channelConfig) {
        super(source);
        this.channelConfig = channelConfig;
    }
}
