package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 停用用户, 类型可以是封禁/锁定
 * @author xxm
 * @since 2023/7/29
 */
@Getter
public class UserDeactivateEvent extends ApplicationEvent {

    private final List<Long> userIds;

    /**
     * @see cn.bootx.platform.iam.code.UserStatusCode
     */
    private final String type;


    public UserDeactivateEvent(Object source, String type, Long ...userIds) {
        super(source);
        this.type = type;
        if (Objects.nonNull(userIds)){
            this.userIds = Arrays.asList(userIds);
        } else {
            this.userIds = null;
        }
    }
    public UserDeactivateEvent(Object source, String type, List<Long> userIds) {
        super(source);
        this.userIds = userIds;
        this.type = type;
    }
}
