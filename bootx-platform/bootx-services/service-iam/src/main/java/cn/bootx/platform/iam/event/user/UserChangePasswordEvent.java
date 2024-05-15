package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 更改用户密码操作
 * @author xxm
 * @since 2023/8/2
 */
@Getter
public class UserChangePasswordEvent extends ApplicationEvent {
    private final List<Long> userIds;

    public UserChangePasswordEvent(Object source, Long ...userIds) {
        super(source);
        if (Objects.nonNull(userIds)){
            this.userIds = Arrays.asList(userIds);
        } else {
            this.userIds = null;
        }
    }

    public UserChangePasswordEvent(Object source, List<Long> userIds) {
        super(source);
        this.userIds = userIds;
    }
}
