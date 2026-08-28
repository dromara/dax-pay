package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.param.auth.ForgetResetPasswordParam;
import cn.daxpay.open.platform.iam.param.auth.ForgetSendCodeParam;
import cn.daxpay.open.platform.iam.result.auth.ForgetSendCodeResult;
import cn.daxpay.open.platform.iam.service.session.OnlineUserService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPasswordPolicyConfig;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/// # 找回密码服务(邮箱验证码方式)
///
/// 登录页自助找回: 账户+邮箱+图形验证码发送验证码 → 验证码+新密码重置;
/// 账户与邮箱须匹配(邮箱为该账户绑定且已验证的邮箱), 单独邮箱不可发起找回;
/// 防账号枚举: 无论账户与邮箱是否匹配已验证, 发码接口统一返回流程ID与一致的成功响应,
/// 不存在的流程在重置环节必然失败, 不泄露账号与邮箱绑定状态;
/// 重置成功后踢掉该用户全部会话(无保留会话)并通知绑定邮箱
@Service
@RequiredArgsConstructor
public class PasswordForgetService {

    private final UserInfoManager userInfoManager;

    private final CaptchaService captchaService;

    private final PasswordDecryptService passwordDecryptService;

    private final PasswordPolicyService passwordPolicyService;

    private final UserPasswordSecurityManager passwordSecurityManager;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final LoginRetryService loginRetryService;

    private final OnlineUserService onlineUserService;

    private final EmailCodeService emailCodeService;

    private final EmailTemplateService emailTemplateService;

    /// 发送找回密码验证码, 返回流程ID(统一响应, 不泄露账号与邮箱绑定状态)
    public ForgetSendCodeResult sendCode(ForgetSendCodeParam param) {
        // 图形验证码校验(防脚本批量探测), 请求防重放由 @NonceVerification 切面处理
        captchaService.validateCaptchaOrThrow(param.getCaptchaKey(), param.getCaptchaCode(), true);
        // 邮件通道预检(未配置时明确报错, 不静默跳过)
        emailTemplateService.checkMailReady();
        String clientCode = ClientEnum.findByCode(param.getClientId())
                .orElseThrow(() -> new BizInfoException("error.iam.email.clientInvalid"))
                .getCode();
        String flowId = UUID.fastUUID().toString(true);
        // 按账户查询(仅邮箱不可定位用户), 邮箱须与账户匹配且已验证才真发验证码, 否则返回哑流程(重置环节必然失败)
        userInfoManager.findByClientCodeAndAccount(clientCode, param.getAccount())
                .filter(user -> param.getEmail().equalsIgnoreCase(user.getEmail()))
                .filter(UserInfo::isEmailVerified)
                .ifPresent(user -> {
                    String code = emailCodeService.generateCode();
                    emailCodeService.save(EmailCodeService.RESET_SCOPE, flowId,
                            new EmailCodeService.EmailCodeContext(code, 0, user.getId(), param.getEmail()));
                    Map<String, Object> params = Map.of(
                            "account", user.getAccount(),
                            "code", code,
                            "expireMinutes", EmailTemplateService.CODE_EXPIRE_MINUTES);
                    emailTemplateService.send(param.getEmail(), user.getId(), EmailTemplateEnum.resetCode, params);
                });
        return new ForgetSendCodeResult().setFlowId(flowId);
    }

    /// 重置密码(验证码校验 + 密码策略/历史校验 + 清登录锁定 + 踢全部会话 + 邮件通知)
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ForgetResetPasswordParam param) {
        // 哑流程(邮箱不存在/未验证)无上下文, 与过期统一按验证码失败处理;
        // 只校验不消费: 密码校验不合格(如与历史重叠)时验证码仍可换码重试, 避免连带烧码
        EmailCodeService.EmailCodeContext context = emailCodeService.verify(
                EmailCodeService.RESET_SCOPE, param.getFlowId(), param.getCode());
        UserInfo userInfo = userInfoManager.findById(context.userId())
                .orElseThrow(UserInfoNotExistsException::new);
        // 解密新密码
        String newPassword = passwordDecryptService.decryptPassword(param.getPassword());
        // 密码策略与历史校验(与登录后改密同链路)
        passwordPolicyService.validatePasswordHistory(userInfo.getId(), newPassword);
        passwordPolicyService.validatePassword(newPassword);
        // 业务校验已全部通过, 此时才消费验证码(删除上下文)
        emailCodeService.consume(EmailCodeService.RESET_SCOPE, param.getFlowId());
        String passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UserInfo update = new UserInfo();
        update.setId(userInfo.getId());
        update.setVersion(userInfo.getVersion());
        update.setPassword(passwordHash);
        userInfoManager.updateById(update);
        // 保存密码历史记录
        passwordPolicyService.savePasswordHistory(userInfo.getId(), passwordHash);
        // 更新密码过期时间, 并清除因连续失败产生的登录锁定
        passwordSecurityManager.updatePasswordExpireTime(userInfo.getId(), this.calculatePasswordExpireTime());
        loginRetryService.unlockAccount(userInfo.getId());
        // 重置成功后踢掉该用户全部会话(找回场景无保留会话), 并通知绑定邮箱
        // 注册事务提交后回调, 避免事务回滚时误踢下线
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                onlineUserService.kickoutAllSessions(userInfo.getId());
                Map<String, Object> noticeParams = Map.of("account", userInfo.getAccount());
                emailTemplateService.send(userInfo.getEmail(), userInfo.getId(), EmailTemplateEnum.resetNotice, noticeParams);
            }
        });
    }

    /// 计算密码过期时间 (UTC)
    private OffsetDateTime calculatePasswordExpireTime() {
        PlatformPasswordPolicyConfig config = iamSecurityConfigService.getPasswordPolicy();
        Integer rotationDays = config.getRotationDays();
        if (rotationDays == null || rotationDays <= 0) {
            return null;
        }
        return OffsetDateTime.now(ZoneOffset.UTC).plusDays(rotationDays);
    }
}
