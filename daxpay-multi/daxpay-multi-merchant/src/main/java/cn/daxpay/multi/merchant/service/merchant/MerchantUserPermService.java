package cn.daxpay.multi.merchant.service.merchant;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.daxpay.multi.service.dao.merchant.MerchantUserManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户用户权限管理
 * @author xxm
 * @since 2024/8/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserPermService {

    private final MerchantUserManager merchantUserManager;
    private final UserRoleService userRoleService;

    /**
     * 分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, List<Long> roleIds) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserId(userId)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        // 分配角色
        userRoleService.saveAssign(userId, roleIds);
    }

}
