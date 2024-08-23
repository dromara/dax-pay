package cn.daxpay.multi.service.service.merchant;

import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.entity.role.Role;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.service.service.UserAdminService;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.daxpay.multi.core.exception.ConfigNotExistException;
import cn.daxpay.multi.service.dao.merchant.MerchantUserManager;
import cn.daxpay.multi.service.entity.merchant.MerchantUser;
import cn.daxpay.multi.service.param.merchant.MerchantAdminParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 用户商户关联关系
 * @author xxm
 * @since 2024/8/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserService {

    private final MerchantUserManager merchantUserManager;

    private final UserAdminService userAdminService;

    private final UserRoleService userRoleService;

    private final RoleManager roleManager;

    /**
     * 根据用户id查询商户号
     */
    @Cacheable(value = "cache:merchant:user", key = "#userId")
    public String findByUserId(Long userId){
        return merchantUserManager.findByField(MerchantUser::getUserId, userId)
                .map(MerchantUser::getMchNo)
                .orElse(null);

    }

    /**
     * 创建商户管理员
     */
    @Transactional(rollbackFor = Exception.class)
    public void createMerchantAdmin(MerchantAdminParam param){

        // 创建用户
        UserInfoParam userInfoParam = new UserInfoParam();
        BeanUtil.copyProperties(param, userInfoParam);
        UserInfo userInfo = userAdminService.add(userInfoParam);
        // 查询角色
        Role admin = roleManager.findByCode("merchant_admin").orElseThrow(() -> new ConfigNotExistException("商户管理员角色不存在, 请检查"));

        // 分配角色
        userRoleService.saveAssign(userInfo.getId(), Collections.singletonList(admin.getId()));
    }

}
