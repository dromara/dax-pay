package cn.daxpay.multi.merchant.service.merchant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.dao.role.RoleManager;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserExpandInfo;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.param.user.UserInfoQuery;
import cn.bootx.platform.iam.result.user.UserWholeInfoResult;
import cn.bootx.platform.iam.service.service.UserAdminService;
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

    private final UserInfoManager userInfoManager;

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
                // 是否允许显示管理员账号
                .eq(UserInfo::isAdministrator, false)
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
     * 添加用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(UserInfoParam userInfoParam) {
        // 创建用户
        UserInfo userInfo = userAdminService.add(userInfoParam);
        // 绑定关系
        merchantUserManager.save(new MerchantUser(userInfo.getId(), MchContextLocal.getMchNo()));

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
}
