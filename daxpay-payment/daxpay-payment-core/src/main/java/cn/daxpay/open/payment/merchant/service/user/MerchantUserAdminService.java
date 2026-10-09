package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.role.RoleCodeEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.auth.service.email.UserEmailService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.role.Role;
import cn.daxpay.open.platform.iam.entity.user.UserExpandInfo;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.result.role.RoleResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.result.user.UserPasswordResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.convert.info.MerchantUserConvert;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantUserQuery;
import cn.daxpay.open.payment.merchant.result.info.MerchantUserResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 商户用户管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserAdminService {

    private final UserInfoManager userInfoManager;
    private final UserExpandInfoManager userExpandInfoManager;
    private final MerchantUserManager merchantUserManager;
    private final MerchantInfoManager merchantInfoManager;
    private final RoleManager roleManager;
    private final PasswordDecryptService passwordDecryptService;
    private final PasswordPolicyService passwordPolicyService;
    private final UserPasswordSecurityManager passwordSecurityManager;
    private final UserRoleService userRoleService;
    private final UserQueryService userQueryService;
    private final UserAdminService userAdminService;
    private final IamSecurityConfigService iamSecurityConfigService;
    private final UserEmailService userEmailService;
    private final PaymentContext paymentContext;
    private final ClientCodeService clientCodeService;

    /// 分页查询商户用户
    public PageResult<MerchantUserResult> page(PageParam pageParam, MerchantUserQuery query) {
        // 商户端强制以上下文 mchNo 覆盖入参, 防止不传或篡改 mchNo 导致跨商户查询
        String contextMchNo = this.getMerchantContextMchNo();
        if (Objects.nonNull(contextMchNo)) {
            query.setMchNo(contextMchNo);
        }
        Page<MerchantUserResult> mpPage = MpUtil.getMpPage(pageParam);
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .innerJoin(MerchantInfo.class, MerchantInfo::getMchNo, MerchantUser::getMchNo)
                .selectAll(UserInfo.class)
                .select(MerchantUser::isAdministrator)
                .select(MerchantInfo::getMchNo)
                .select(MerchantInfo::getMchName)
                .eq(StrUtil.isNotBlank(query.getMchNo()), MerchantInfo::getMchNo, query.getMchNo())
                .eq(StrUtil.isNotBlank(query.getStatus()), UserInfo::getStatus, query.getStatus())
                .like(StrUtil.isNotBlank(query.getName()), UserInfo::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getAccount()), UserInfo::getAccount, query.getAccount());
        Page<MerchantUserResult> page = userInfoManager.selectJoinListPage(mpPage, MerchantUserResult.class, wrapper);
        // 回填主体管理员标识: join 查询中 mch_user.administrator 与 iam_user_info.administrator(内置超管标志) 同名,
        // 同名列会被用户表的值(商户用户恒 false)遮蔽, 按关联表批量回填真实标识供前端"管理员"tag与操作禁用使用
        List<Long> userIds = page.getRecords().stream().map(MerchantUserResult::getId).toList();
        if (!userIds.isEmpty()) {
            Map<Long, Boolean> adminMap = merchantUserManager.findAllByFields(MerchantUser::getUserId, userIds).stream()
                    .collect(Collectors.toMap(MerchantUser::getUserId, MerchantUser::isAdministrator, (a, b) -> a));
            page.getRecords().forEach(r -> r.setAdministrator(adminMap.getOrDefault(r.getId(), false)));
        }
        return MpUtil.toPageResult(page);
    }

    /// 根据用户ID查询用户详情
    public UserInfoResult findById(Long id) {
        this.checkMerchantUser(id);
        return userInfoManager.findById(id)
                .map(UserInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /// 添加商户用户
    /// @return 含初始密码明文的结果(未传密码时由系统生成随机密码)
    @Transactional(rollbackFor = Exception.class)
    public UserPasswordResult add(MerchantUserParam param) {
        // 商户端只能为当前商户创建子账号, mchNo 以上下文为准不信任入参
        String contextMchNo = this.getMerchantContextMchNo();
        if (Objects.nonNull(contextMchNo)) {
            param.setMchNo(contextMchNo);
        }
        String mchNo = param.getMchNo();
        MerchantInfo merchantInfo = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.merchantNotExist"));

        // 校验账号唯一性（商户终端）
        String clientCode = ClientEnum.MERCHANT.getCode();
        if (userQueryService.existsAccountByClientCode(clientCode, param.getAccount())) {
            // 商户: 该账号已存在
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.accountExists");
        }

        // 密码可选: 未传时生成随机密码, 传入时按 RSA 密文解密(兼容存量调用方)
        String password = StrUtil.isBlank(param.getPassword())
                ? passwordPolicyService.generateSecurePassword()
                : passwordDecryptService.decryptPassword(param.getPassword());
        passwordPolicyService.validatePassword(password);

        // 创建用户
        UserInfo userInfo = MerchantUserConvert.CONVERT.toEntity(param);
        userInfo.setClientCode(clientCode)
                .setAdministrator(false)
                .setStatus(UserStatusEnum.NORMAL.getCode());
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        userInfo.setPassword(passwordHash);
        userInfoManager.save(userInfo);

        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);

        // 创建用户扩展信息
        OffsetDateTime passwordExpireTime = this.calculatePasswordExpireTime();
        passwordSecurityManager.initPasswordSecurity(userInfo.getId(), passwordExpireTime);

        UserExpandInfo userExpandInfo = new UserExpandInfo()
                .setRegisterTime(OffsetDateTime.now(ZoneOffset.UTC));
        userExpandInfo.setId(userInfo.getId());
        userExpandInfoManager.save(userExpandInfo);

        // 绑定角色: 指定用指定角色, 未指定默认内置商户普通用户(merchant_user);
        // 创建即有角色, 防止出现无角色裸用户(登录后菜单为空跳转404)
        Role role;
        if (Objects.nonNull(param.getRoleId())) {
            role = roleManager.findById(param.getRoleId())
                    // 商户: 指定的角色不存在
                    .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.roleNotExist"));
        } else {
            role = roleManager.findByCode(RoleCodeEnum.MERCHANT_USER.getCode())
                    .orElseThrow(ConfigNotExistException::new);
        }
        // 时序: 先绑角色后存挂靠关联(主体管理员保护按挂靠判定, 子用户挂靠 administrator=false 本不触发,
        // 统一时序防未来调整时误伤; 终端一致性由 saveAssign 校验, 不可跨终端指定角色)
        userRoleService.saveAssign(userInfo.getId(), role.getId(), true);

        // 创建商户用户关联
        MerchantUser merchantUser = new MerchantUser(userInfo.getId(), mchNo, false);
        merchantUserManager.save(merchantUser);

        // 返回账号与初始密码明文, 供管理员一次性复制转告用户
        return new UserPasswordResult()
                .setUserId(userInfo.getId())
                .setAccount(userInfo.getAccount())
                .setName(userInfo.getName())
                .setPassword(password);
    }

    /// 编辑商户用户
    @Transactional(rollbackFor = Exception.class)
    public void update(MerchantUserParam param) {
        UserInfo userInfo = userInfoManager.findById(param.getId())
                .orElseThrow(UserInfoNotExistsException::new);

        MerchantUser merchantUser = merchantUserManager.findByUserId(param.getId())
                // 商户: 商户用户关联关系不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist"));
        // 商户端校验目标用户归属当前商户, 防止跨商户篡改
        this.checkBelongCurrentMerchant(merchantUser);

        param.setPassword(null);
        param.setAccount(null);
        MerchantUserConvert.CONVERT.copy(param, userInfo);
        userInfoManager.updateById(userInfo);
    }

    /// 强制解绑商户用户邮箱
    /// 用户邮箱本体失效、无法走本人解绑流程(密码+旧邮箱验证码)时的管理员代管通道;
    /// 仅清空邮箱, 不可指定新邮箱, 新邮箱只能由用户本人走绑定验证流程获得
    @Transactional(rollbackFor = Exception.class)
    public void unbindEmail(Long userId) {
        this.checkMerchantUser(userId);
        userEmailService.adminUnbind(userId);
    }

    /// 分配角色
    ///
    /// 商户管理员(主体侧操作者)可给子用户分配本终端任意角色: ignoreScopes=true 放行"操作者须持有该角色"的范围校验
    /// (单角色模式下商户管理员仅持有 merchant_admin, 不放行则永远无法分配其他内置/自建角色);
    /// 跨终端角色仍被 saveAssign 的终端一致性校验拦截, 目标为商户管理员的改角色被主体管理员保护拦截
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, Long roleId) {
        this.checkMerchantUser(userId);
        userRoleService.saveAssign(userId, roleId, true);
    }

    /// 查询用户可分配的角色列表(按目标用户终端), 商户端校验归属防止跨商户探测
    public List<RoleResult> findAssignableRolesByUser(Long userId) {
        this.checkMerchantUser(userId);
        return userRoleService.findAssignableRolesByUser(userId);
    }

    /// 查询用户已分配的角色ID集合(单角色模式), 商户端校验归属防止跨商户探测
    public List<Long> findRoleIdsByUser(Long userId) {
        this.checkMerchantUser(userId);
        return userRoleService.findRoleIdsByUser(userId);
    }

    /// 封禁商户用户
    public void ban(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.ban(userId);
    }

    /// 批量封禁商户用户
    public void banBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.banBatch(userIds);
    }

    /// 解锁商户用户
    public void unlock(Long userId) {
        this.checkMerchantUser(userId);
        userAdminService.unlock(userId);
    }

    /// 批量解锁商户用户
    public void unlockBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        userAdminService.unlockBatch(userIds);
    }

    /// 重置密码
    /// 密码统一由系统按密码策略随机生成, 不接受调用方指定
    /// @return 含初始密码明文的结果
    @Transactional(rollbackFor = Exception.class)
    public UserPasswordResult restartPassword(Long userId) {
        this.checkMerchantUser(userId);
        return userAdminService.restartPassword(userId);
    }

    /// 批量重置密码
    /// @return 每个用户独立的初始密码结果
    @Transactional(rollbackFor = Exception.class)
    public List<UserPasswordResult> restartPasswordBatch(List<Long> userIds) {
        this.checkMerchantUser(userIds);
        return userAdminService.restartPasswordBatch(userIds);
    }

    /// 按平台密码策略计算过期时间(UTC); 未启用轮换则 null
    private OffsetDateTime calculatePasswordExpireTime() {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy();
        Integer rotationDays = config.getRotationDays();
        if (Objects.isNull(rotationDays) || rotationDays <= 0) {
            return null;
        }
        return OffsetDateTime.now(ZoneOffset.UTC).plusDays(rotationDays);
    }

    /// 获取商户端上下文中的商户号
    /// 仅 [ClientEnum#MERCHANT] 终端返回上下文 mchNo(由 MchContextLocalFilter 装载);
    /// 运营端返回 null 表示不介入(运营是平台管理员, 无行级隔离);
    /// 商户端上下文缺失时 fail-closed 拒绝操作
    private String getMerchantContextMchNo() {
        if (!ClientEnum.MERCHANT.getCode().equals(clientCodeService.getClientCode())) {
            return null;
        }
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 支付: 商户上下文未装载
            throw new BizException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验商户用户归属当前商户(仅商户端生效), 防止跨商户横向越权
    private void checkBelongCurrentMerchant(MerchantUser merchantUser) {
        String contextMchNo = this.getMerchantContextMchNo();
        if (Objects.nonNull(contextMchNo) && !contextMchNo.equals(merchantUser.getMchNo())) {
            // 商户: 商户用户不属于当前商户
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserNotBelongCurrent");
        }
    }

    /// 校验用户是否属于商户, 商户端同时校验归属当前商户
    private void checkMerchantUser(Long userId) {
        MerchantUser merchantUser = merchantUserManager.findByUserId(userId)
                // 商户: 商户用户关联关系不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist"));
        this.checkBelongCurrentMerchant(merchantUser);
    }

    /// 批量校验用户是否属于商户, 商户端同时校验归属当前商户
    private void checkMerchantUser(List<Long> userIds) {
        // findAllByFields 走 in 查询; findAllByField 是单值 eq, 传 List 会触发 PG TypeException
        List<MerchantUser> users = merchantUserManager.findAllByFields(MerchantUser::getUserId, userIds);
        if (users.size() != userIds.size()) {
            // 商户: 商户用户关联关系不存在
            throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchUserRelationNotExist");
        }
        users.forEach(this::checkBelongCurrentMerchant);
    }
}
