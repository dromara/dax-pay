package org.dromara.daxpay.service.merchant.service.info;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.iam.service.user.UserAdminService;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.merchant.dao.info.MerchantUserManager;
import org.dromara.daxpay.service.merchant.entity.info.MerchantUser;
import org.dromara.daxpay.service.merchant.param.info.MerchantUserQuery;
import org.dromara.daxpay.service.merchant.result.info.MerchantUserResult;
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
    private final UserInfoManager userInfoManager;

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
     * 分页
     */
    public PageResult<MerchantUserResult> page(PageParam pageParam, MerchantUserQuery query){
        Page<MerchantUserResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<MerchantUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(UserInfo.class,UserInfo::getId, MerchantUser::getUserId)
                .selectAll(UserInfo.class)
                .select(MerchantUser::getMchNo)
                .like(StrUtil.isNotBlank(query.getAccount()),UserInfo::getAccount, query.getAccount())
                .like(StrUtil.isNotBlank(query.getName()),UserInfo::getName, query.getName());
        Page<MerchantUserResult> page = merchantUserManager.selectJoinListPage(mpPage, MerchantUserResult.class, wrapper);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据Id查询
     */
    public UserInfoResult findById(Long id) {
        this.checkMerchantUser(id);
        return userInfoManager.findById(id)
                .map(UserInfo::toResult)
                .orElseThrow(() -> new DataNotExistException("用户不存在"));
    }

    /**
     * 封禁用户
     */
    public void ban(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.ban(userId);
    }

    /**
     * 批量封禁用户
     */
    public void banBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.banBatch(userIds);
    }

    /**
     * 锁定用户
     */
    public void lock(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.lock(userId);
    }

    /**
     * 批量锁定用户
     */
    public void lockBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.lockBatch(userIds);
    }

    /**
     * 解锁用户
     */
    public void unlock(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.unlock(userId);
    }

    /**
     * 批量解锁用户
     */
    public void unlockBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.unlockBatch(userIds);
    }


    /**
     * 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(Long userId, String newPassword) {
        this.checkMerchantUser(userId);
        userAdminService.restartPassword(userId,newPassword);
    }

    /**
     * 批量重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void restartPasswordBatch(List<Long> userIds, String newPassword){
        this.checkMerchantUser(userIds);
        userAdminService.restartPasswordBatch(userIds,newPassword);
    }

    /**
     * 编辑用户信息
     */
    public void update(UserInfoParam userInfoParam) {
        this.checkMerchantUser(userInfoParam.getId());
        userAdminService.update(userInfoParam);
    }

    /**
     * 判断是否为商户用户
     */
    private void checkMerchantUser(Long userId){
        if (!merchantUserManager.existedByField(MerchantUser::getUserId, userId)){
            throw new OperationFailException("无法对当前用户进行操作");
        }
    }

    /**
     * 判断是否为商户用户
     */
    private void checkMerchantUser(List<Long> userIds){
        List<MerchantUser> users = merchantUserManager.findAllByField(MerchantUser::getUserId, userIds);
        if (users.size() != userIds.size()){
            throw new OperationFailException("无法对当前用户进行操作");
        }
    }
}
