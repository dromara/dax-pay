package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 重置用户密码操作
 * @author xxm
 * @since 2023/7/31
 */
@Getter
public class UserRestartPasswordEvent extends ApplicationEvent {
    private final List<Long> userIds;

    public UserRestartPasswordEvent(Object source, Long ...userIds) {
        super(source);
        if (Objects.nonNull(userIds)){
            this.userIds = Arrays.asList(userIds);
        } else {
            this.userIds = null;
        }
    }
    public UserRestartPasswordEvent(Object source, List<Long> userIds) {
        super(source);
        this.userIds = userIds;
    }
}
