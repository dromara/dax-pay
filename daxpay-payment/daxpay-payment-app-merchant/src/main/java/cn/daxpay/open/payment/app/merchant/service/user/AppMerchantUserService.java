package cn.daxpay.open.payment.app.merchant.service.user;

import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserAdminService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-商户用户服务
///
/// 转发至 [MerchantUserAdminService]。
/// 商户归属由 core 层统一防护: mchNo 取自 [cn.daxpay.open.payment.common.context.PaymentContext],
/// 不信任入参; 单条操作校验目标用户归属当前商户, 防止跨商户越权。
@Service
@RequiredArgsConstructor
public class AppMerchantUserService {

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
    public UserPasswordResult add(MerchantUserParam param) {
        return merchantUserAdminService.add(param);
    }

    /// 修改
    public void update(MerchantUserParam param) {
        merchantUserAdminService.update(param);
    }

    /// 强制解绑邮箱
    /// 仅清空邮箱与验证状态, 不可指定新邮箱(邮箱变更只能由用户本人走绑定验证流程)
    public void unbindEmail(Long userId) {
        merchantUserAdminService.unbindEmail(userId);
    }

    /// 分配角色
    public void assignRole(Long userId, Long roleId) {
        merchantUserAdminService.assignRole(userId, roleId);
    }

    /// 封禁
    public void ban(Long userId) {
        merchantUserAdminService.ban(userId);
    }

    /// 解锁
    public void unlock(Long userId) {
        merchantUserAdminService.unlock(userId);
    }

    /// 重置密码
    public UserPasswordResult restartPassword(Long userId) {
        return merchantUserAdminService.restartPassword(userId);
    }
}
