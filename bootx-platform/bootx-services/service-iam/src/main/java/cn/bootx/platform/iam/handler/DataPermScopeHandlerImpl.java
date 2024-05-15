package cn.bootx.platform.iam.handler;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.iam.core.upms.service.UserDataRoleService;
import cn.bootx.platform.starter.data.perm.exception.NotLoginPermException;
import cn.bootx.platform.starter.data.perm.local.DataPermContextHolder;
import cn.bootx.platform.starter.data.perm.scope.DataPermScope;
import cn.bootx.platform.starter.data.perm.scope.DataPermScopeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 数据权限业务实现
 *
 * @author xxm
 * @since 2021/12/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataPermScopeHandlerImpl implements DataPermScopeHandler {

    /**
     * 需要进行懒加载方式的注入, 因为 DataPermScopeHandler bean创建时机比UserDataScopeService早
     */
    @Lazy
    private final UserDataRoleService userDataRoleService;

    /**
     * 获取数据权限范围配置
     */
    @Override
    public DataPermScope getDataPermScope() {
        Long userId = DataPermContextHolder.getUserDetail()
            .map(UserDetail::getId)
            .orElseThrow(NotLoginPermException::new);
        return userDataRoleService.getDataPermScopeByUser(userId);
    }

}
