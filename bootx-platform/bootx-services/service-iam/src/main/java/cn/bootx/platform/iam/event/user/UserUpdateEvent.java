package cn.bootx.platform.iam.event.user;

import cn.bootx.platform.iam.dto.user.UserInfoDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户信息更新事件
 * @author xxm
 * @since 2023/7/31
 */
@Getter
public class UserUpdateEvent extends ApplicationEvent {
    private final UserInfoDto userInfo;

    public UserUpdateEvent(Object source, UserInfoDto userInfo) {
        super(source);
        this.userInfo = userInfo;
    }

}
