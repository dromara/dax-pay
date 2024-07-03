package cn.bootx.platform.iam.service.upms;

import cn.bootx.platform.iam.dao.permission.RoleCodeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限码关联关系
 * @author xxm
 * @since 2024/7/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleCodeService {
    private final RoleCodeManager roleCodeManager;

    /**
     * 保存
     */
    public void saveAssign(Long roleId, List<Long> permissionIds, boolean updateAddChildren){

    }


}
