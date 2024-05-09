package cn.bootx.platform.iam.event.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 用户分配角色事件
 * @author xxm
 * @since 2023/8/3
 */
@Getter
public class UserAssignRoleEvent extends ApplicationEvent {
    private final List<Long> userIds;
    private final List<Long> roleIds;

    public UserAssignRoleEvent(Object source, List<Long> userIds, List<Long> roleIds) {
        super(source);
        this.userIds = userIds;
        this.roleIds = roleIds;
    }
}
