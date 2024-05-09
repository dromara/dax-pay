package cn.bootx.platform.iam.core.dept.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 部门删除事件
 *
 * @author xxm
 * @since 2022/1/3
 */
@Getter
public class DeptDeleteEvent extends ApplicationEvent {

    private final List<Long> deptIds;

    public DeptDeleteEvent(Object source, List<Long> deptIds) {
        super(source);
        this.deptIds = deptIds;
    }

}
