package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用户删除事件
 * @author xxm
 * @since 2023/7/31
 */
@Getter
public class UserDeleteEvent extends ApplicationEvent {
    private final List<Long> userIds;

    public UserDeleteEvent(Object source, Long ...userIds) {
        super(source);
        if (Objects.nonNull(userIds)){
            this.userIds = Arrays.asList(userIds);
        } else {
            this.userIds = null;
        }
    }

    public UserDeleteEvent(Object source, List<Long> userIds) {
        super(source);
        this.userIds = userIds;
    }
}
