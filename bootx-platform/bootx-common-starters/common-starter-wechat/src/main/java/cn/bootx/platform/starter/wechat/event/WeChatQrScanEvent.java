package cn.bootx.platform.starter.wechat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 微信扫码关注事件
 *
 * @author xxm
 * @since 2022/8/4
 */
@Getter
public class WeChatQrScanEvent extends ApplicationEvent {

    /** 微信id */
    private final String openId;

    /** key值 */
    private final String eventKey;

    public WeChatQrScanEvent(Object source, String openId, String eventKey) {
        super(source);
        this.openId = openId;
        this.eventKey = eventKey;
    }

}
