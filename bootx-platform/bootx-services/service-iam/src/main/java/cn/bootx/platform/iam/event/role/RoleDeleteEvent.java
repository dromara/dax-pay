package cn.bootx.platform.iam.event.role;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 角色删除事件
 * @author xxm
 * @since 2023/8/2
 */
@Getter
public class RoleDeleteEvent extends ApplicationEvent {
    private final List<Long> roleIds;


    public RoleDeleteEvent(Object source, Long ...roleIds) {
        super(source);
        if (Objects.nonNull(roleIds)){
            this.roleIds = Arrays.asList(roleIds);
        } else {
            this.roleIds = null;
        }
    }
    public RoleDeleteEvent(Object source, List<Long> roleIds) {
        super(source);
        this.roleIds = roleIds;
    }
}
