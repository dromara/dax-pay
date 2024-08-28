package cn.daxpay.multi.merchant.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.iam.result.role.RoleResult;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.iam.result.user.UserWholeInfoResult;
import cn.bootx.platform.iam.service.service.UserAdminService;
import cn.bootx.platform.iam.service.service.UserQueryService;
import cn.bootx.platform.iam.service.upms.UserRoleService;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.dao.merchant.MerchantUserManager;
import cn.daxpay.multi.service.entity.merchant.MerchantUser;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private final UserQueryService userQueryService;

    private final UserRoleService userRoleService;

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
     * 分页查询
     */
    public PageResult<UserWholeInfoResult> page(PageParam pageParam, UserInfoQuery query) {

        Page<UserWholeInfoResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<MerchantUser> wrapper = new MPJLambdaWrapper<>();
        // 连表: 商户用户表, 用户表、用户扩展信息表
        wrapper.innerJoin(UserInfo.class,UserInfo::getId, MerchantUser::getUserId)
                .innerJoin(UserExpandInfo.class,UserExpandInfo::getId, UserInfo::getId)
                .selectAll(UserInfo.class)
                .selectAll(UserExpandInfo.class)
                // 不允许显示超级管理员账号
                .eq(UserInfo::isAdministrator, false)
                // 不允许显示商户管理员账号
                .eq(MerchantUser::isAdministrator, false)
                .eq(MerchantUser::getMchNo, MchContextLocal.getMchNo())
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getAccount()), UserInfo::getAccount, query.getAccount())
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName());
        Page<UserWholeInfoResult> page = merchantUserManager.selectJoinListPage(mpPage,UserWholeInfoResult.class,wrapper);

        return new PageResult<UserWholeInfoResult>()
                .setRecords(page.getRecords())
                .setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setTotal(page.getTotal());
    }


    /**
     * 获取用户信息
     */
    public UserInfoResult findById(Long id) {
        if (!merchantUserManager.existsByUserId(id)){
            throw new ValidationFailedException("用户不属于该商户");
        }
        return userQueryService.findById(id);
    }

    /**
     * 添加用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(UserInfoParam userInfoParam) {
        // 创建用户
        UserInfo userInfo = userAdminService.add(userInfoParam);
        // 绑定关系
        merchantUserManager.save(new MerchantUser(userInfo.getId(), MchContextLocal.getMchNo(),false));

    }

    /**
     * 更新用户信息
     */
    public void update(UserInfoParam userInfoParam) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserId(userInfoParam.getId())) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        userAdminService.update(userInfoParam);
    }

    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(Long userId, String newPassword) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserId(userId)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        userAdminService.restartPassword(userId, newPassword);
    }

    /**
     * 批量重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPasswordBatch(List<Long> userIds, String newPassword){
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserIds(userIds)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        userAdminService.restartPasswordBatch(userIds, newPassword);
    }


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


    /**
     * 分配角色 批量
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleBatch(List<Long> userIds, List<Long> roleIds) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserIds(userIds)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        // 分配角色
        userRoleService.saveAssignBatch(userIds, roleIds);
    }

    /**
     * 根据用户ID获取到角色集合
     */
    public List<RoleResult> findRolesByUser(Long userId) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserId(userId)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        return userRoleService.findRolesByUser(userId);
    }

    /**
     * 根据用户ID获取到角色id集合
     */
    public List<Long> findRoleIdsByUser(Long userId) {
        // 判断用户是否属于该商户
        if (!merchantUserManager.existsByUserId(userId)) {
            throw new ValidationFailedException("用户不属于该商户");
        }
        return userRoleService.findRoleIdsByUser(userId);
    }
}
