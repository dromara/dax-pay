package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 解锁用户
 * @author xxm
 * @since 2023/7/29
 */
@Getter
public class UserUnlockEvent extends ApplicationEvent {

    private final List<Long> userIds;

    public UserUnlockEvent(Object source, Long ...userIds) {
        super(source);
        if (Objects.nonNull(userIds)){
            this.userIds = Arrays.asList(userIds);
        } else {
            this.userIds = null;
        }
    }
    public UserUnlockEvent(Object source, List<Long> userIds) {
        super(source);
        this.userIds = userIds;
    }
}
