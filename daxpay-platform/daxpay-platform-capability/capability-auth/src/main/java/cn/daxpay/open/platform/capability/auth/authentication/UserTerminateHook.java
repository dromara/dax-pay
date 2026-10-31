package cn.daxpay.open.platform.capability.auth.authentication;

import java.util.List;

/// # 用户注销挂靠钩子
///
/// 用户注销([cn.daxpay.open.platform.iam.service.user.UserAdminService#delete])前,
/// 需要感知用户在业务域(商户)的挂靠关系; 业务挂靠表在 payment 域,
/// platform 不能编译依赖 payment, 故采用本 SPI: 业务侧提供实现, iam 侧同容器注入,
/// 模式对齐 [UserInfoStatusCheck](登录状态检查由 payment 的 DaxUserInfoStatusCheck 实现)。
///
/// 口径: 挂靠非空时 iam 通用入口**拒删**(引导去对应主体的用户管理操作),
/// 关联行的删除由主体侧服务在调用注销前完成——本接口只做查询, 不提供清理方法,
/// 防止绕过主体上下文直接清挂靠。
public interface UserTerminateHook {

    /// 查询用户当前的全部主体挂靠描述
    ///
    /// @param userId 用户ID
    /// @return 挂靠描述列表(如 "商户 M1234"), 无挂靠返回空列表
    List<String> findAffiliations(Long userId);
}
