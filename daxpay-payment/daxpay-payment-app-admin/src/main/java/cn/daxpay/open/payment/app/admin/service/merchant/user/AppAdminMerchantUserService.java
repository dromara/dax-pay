package cn.daxpay.open.payment.app.admin.service.merchant.user;

import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserAdminService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-商户用户服务
///
/// 转发至 [MerchantUserAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminMerchantUserService {

    private final MerchantUserAdminService merchantUserAdminService;

    /// 分页
    public PageResult<MerchantUserResult> page(PageParam pageParam, MerchantUserQuery query) {
        return merchantUserAdminService.page(pageParam, query);
    }

    /// 详情
    public UserInfoResult findById(Long id) {
        return merchantUserAdminService.findById(id);
    }

    /// 新增
    public void add(MerchantUserParam param) {
        merchantUserAdminService.add(param);
    }

    /// 修改
    public void update(MerchantUserParam param) {
        merchantUserAdminService.update(param);
    }

    /// 分配角色
    public void assignRole(Long userId, Long roleId) {
        merchantUserAdminService.assignRole(userId, roleId);
    }

    /// 封禁
    public void ban(Long userId) {
        merchantUserAdminService.ban(userId);
    }

    /// 批量封禁
    public void banBatch(List<Long> userIds) {
        merchantUserAdminService.banBatch(userIds);
    }

    /// 解锁
    public void unlock(Long userId) {
        merchantUserAdminService.unlock(userId);
    }

    /// 批量解锁
    public void unlockBatch(List<Long> userIds) {
        merchantUserAdminService.unlockBatch(userIds);
    }

    /// 重置密码
    public void restartPassword(Long userId, String newPassword) {
        merchantUserAdminService.restartPassword(userId, newPassword);
    }

    /// 批量重置密码
    public void restartPasswordBatch(List<Long> userIds, String newPassword) {
        merchantUserAdminService.restartPasswordBatch(userIds, newPassword);
    }
}
