package cn.bootx.platform.iam.event.role;

import cn.bootx.platform.iam.dto.role.RoleDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 角色更新事件
 * @author xxm
 * @since 2023/7/31
 */
@Getter
public class RoleUpdateEvent extends ApplicationEvent {
    private final RoleDto role;
    public RoleUpdateEvent(Object source, RoleDto role) {
        super(source);
        this.role = role;
    }
}
