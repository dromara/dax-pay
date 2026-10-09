package cn.daxpay.open.platform.capability.auth.authentication;

/// # 主体管理员保护钩子
///
/// 商户的**主体管理员**([cn.daxpay.open.payment.merchant.entity.info.MerchantUser]
/// 的 administrator=true 行)角色固定、不可被封禁, 角色变更与封禁入口在 iam 域
/// ([cn.daxpay.open.platform.iam.service.upms.UserRoleService#saveAssign]、
/// [cn.daxpay.open.platform.iam.service.user.UserAdminService])需要感知该标识;
/// 主体挂靠表在 payment 域, platform 不能编译依赖 payment, 故采用本 SPI:
/// 业务侧提供实现(DaxSubjectAdminGuardHook), iam 侧同容器注入。
///
/// 口径: 主体管理员的角色在创建主体时由 [cn.daxpay.open.platform.core.enums.role.RoleCodeEnum]
/// 对应 *_ADMIN 内置角色一次绑定, 之后**任何入口不可改换**(防降权/防提权),
/// 变更管理员须走主体主档的专用换绑通道(暂未提供); 封禁/锁定同理拒绝, 防主体失管。
///
/// 时序注意: 主体创建链路(saveAssign 初始绑定)必须**先绑角色后存挂靠关联**,
/// 否则本钩子查询会把初始化绑定误判为"改管理员角色"而拒绝。
public interface SubjectAdminGuardHook {

    /// 查询用户是否为某主体的管理员
    ///
    /// @param userId 用户ID
    /// @return 主体管理员描述(如 "商户 M1234"), 非管理员返回 null
    String findSubjectAdmin(Long userId);
}
