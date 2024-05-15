package cn.bootx.platform.notice.event.sms;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 短信通道停用事件
 * @author xxm
 * @since 2023/8/5
 */
@Getter
public class SmsChannelDisableEnableEvent extends ApplicationEvent {
    private final String code;

    public SmsChannelDisableEnableEvent(Object source, String code) {
        super(source);
        this.code = code;
    }
}
