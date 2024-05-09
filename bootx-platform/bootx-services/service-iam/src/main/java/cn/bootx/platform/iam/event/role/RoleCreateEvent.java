package cn.bootx.platform.iam.event.role;

import cn.bootx.platform.iam.dto.role.RoleDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 角色创建时间
 * @author xxm
 * @since 2023/7/31
 */
@Getter
public class RoleCreateEvent extends ApplicationEvent {
    private final RoleDto role;
    public RoleCreateEvent(Object source, RoleDto role) {
        super(source);
        this.role = role;
    }
}
