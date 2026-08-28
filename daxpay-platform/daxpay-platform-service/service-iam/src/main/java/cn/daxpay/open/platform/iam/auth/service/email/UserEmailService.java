package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.param.user.EmailBindConfirmParam;
import cn.daxpay.open.platform.iam.param.user.EmailBindSendCodeParam;
import cn.daxpay.open.platform.iam.param.user.EmailUnbindParam;
import cn.daxpay.open.platform.iam.param.user.EmailUnbindSendCodeParam;
import cn.daxpay.open.platform.iam.result.user.EmailInfoResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # 用户邮箱绑定服务
///
/// 邮箱作为找回密码的安全凭证, 绑定/换绑/解绑是敏感操作:
/// 绑定走"登录密码确认 + 新邮箱验证码"双确认(证明账号所有权 + 新邮箱持有性),
/// 解绑走"登录密码确认 + 旧邮箱验证码"双确认(证明账号所有权 + 旧邮箱持有性, 与绑定对称),
/// 解绑成功后通知旧邮箱; email 字段的变更仅允许经本服务发生,
/// 基础信息修改接口不再受理 email 变更
@Service
@RequiredArgsConstructor
public class UserEmailService {

    private final UserInfoManager userInfoManager;

    private final PasswordDecryptService passwordDecryptService;

    private final LoginRetryService loginRetryService;

    private final EmailCodeService emailCodeService;

    private final EmailTemplateService emailTemplateService;

    /// 查询当前用户邮箱绑定状态
    public EmailInfoResult getEmailInfo() {
        UserInfo userInfo = this.currentUser();
        return new EmailInfoResult()
                .setEmail(userInfo.getEmail())
                .setEmailVerified(userInfo.isEmailVerified());
    }

    /// 发送邮箱绑定/换绑验证码(登录密码确认 + 端内唯一性校验)
    public void sendBindCode(EmailBindSendCodeParam param) {
        UserInfo userInfo = this.currentUser();
        // 登录密码确认(防会话劫持后篡改找回通道)
        this.verifyPassword(userInfo, param.getPassword());
        String newEmail = param.getEmail();
        // 与当前已验证绑定邮箱相同时无需重复绑定
        if (newEmail.equals(userInfo.getEmail()) && userInfo.isEmailVerified()) {
            // 邮箱: 该邮箱已是当前账号的绑定邮箱
            throw new BizInfoException("error.iam.email.sameAsCurrent");
        }
        // 端内唯一性校验(排除自身)
        if (userInfoManager.existsByClientCodeAndEmail(userInfo.getClientCode(), newEmail, userInfo.getId())) {
            // 权限: 该终端下邮箱已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedByOtherInClient");
        }
        // 邮件通道预检(未配置时明确报错, 不静默跳过)
        emailTemplateService.checkMailReady();
        // 保存验证码上下文并发送验证码邮件(5分钟有效)
        String code = emailCodeService.generateCode();
        emailCodeService.save(EmailCodeService.BIND_SCOPE, String.valueOf(userInfo.getId()),
                new EmailCodeService.EmailCodeContext(code, 0, userInfo.getId(), newEmail));
        Map<String, Object> params = Map.of(
                "account", userInfo.getAccount(),
                "code", code,
                "expireMinutes", EmailTemplateService.CODE_EXPIRE_MINUTES);
        emailTemplateService.send(newEmail, userInfo.getId(), EmailTemplateEnum.bindCode, params);
    }

    /// 确认绑定(验证码校验通过后生效, 换绑成功时通知旧邮箱)
    public void bindConfirm(EmailBindConfirmParam param) {
        UserInfo userInfo = this.currentUser();
        // 只校验不消费: 唯一性兜底失败时验证码仍可重试, 避免连带烧码
        EmailCodeService.EmailCodeContext context = emailCodeService.verify(
                EmailCodeService.BIND_SCOPE, String.valueOf(userInfo.getId()), param.getCode());
        String newEmail = context.email();
        // 验证码有效期内唯一性可能被其他账号抢占, 生效前兜底再校验一次
        if (userInfoManager.existsByClientCodeAndEmail(userInfo.getClientCode(), newEmail, userInfo.getId())) {
            // 权限: 该终端下邮箱已被其他用户使用
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.emailUsedByOtherInClient");
        }
        // 业务校验已全部通过, 此时才消费验证码(删除上下文)
        emailCodeService.consume(EmailCodeService.BIND_SCOPE, String.valueOf(userInfo.getId()));
        String oldEmail = userInfo.getEmail();
        UserInfo update = new UserInfo();
        update.setId(userInfo.getId());
        update.setVersion(userInfo.getVersion());
        update.setEmail(newEmail);
        update.setEmailVerified(true);
        userInfoManager.updateById(update);
        // 换绑成功后通知旧邮箱(旧邮箱存在且与新邮箱不同)
        if (StrUtil.isNotBlank(oldEmail) && !oldEmail.equals(newEmail)) {
            Map<String, Object> params = Map.of(
                    "account", userInfo.getAccount(),
                    "newEmail", newEmail);
            emailTemplateService.send(oldEmail, userInfo.getId(), EmailTemplateEnum.changeNotice, params);
        }
    }

    /// 发送邮箱解绑验证码(登录密码确认, 验证码发至当前绑定邮箱)
    public void sendUnbindCode(EmailUnbindSendCodeParam param) {
        UserInfo userInfo = this.currentUser();
        // 登录密码确认(防会话劫持后切断找回通道)
        this.verifyPassword(userInfo, param.getPassword());
        if (StrUtil.isBlank(userInfo.getEmail())) {
            // 邮箱: 当前账号未绑定邮箱
            throw new BizInfoException("error.iam.email.notBound");
        }
        // 邮件通道预检(未配置时明确报错, 不静默跳过)
        emailTemplateService.checkMailReady();
        // 保存验证码上下文并发送验证码邮件(5分钟有效)
        String code = emailCodeService.generateCode();
        emailCodeService.save(EmailCodeService.UNBIND_SCOPE, String.valueOf(userInfo.getId()),
                new EmailCodeService.EmailCodeContext(code, 0, userInfo.getId(), userInfo.getEmail()));
        Map<String, Object> params = Map.of(
                "account", userInfo.getAccount(),
                "code", code,
                "expireMinutes", EmailTemplateService.CODE_EXPIRE_MINUTES);
        emailTemplateService.send(userInfo.getEmail(), userInfo.getId(), EmailTemplateEnum.unbindCode, params);
    }

    /// 解绑邮箱(登录密码 + 旧邮箱验证码双确认, 解绑后该邮箱不可再用于找回密码, 并通知旧邮箱)
    public void unbind(EmailUnbindParam param) {
        UserInfo userInfo = this.currentUser();
        this.verifyPassword(userInfo, param.getPassword());
        if (StrUtil.isBlank(userInfo.getEmail())) {
            // 邮箱: 当前账号未绑定邮箱
            throw new BizInfoException("error.iam.email.notBound");
        }
        // 解绑验证码校验(单次消费, 与绑定/找回同机制)
        emailCodeService.verifyAndConsume(
                EmailCodeService.UNBIND_SCOPE, String.valueOf(userInfo.getId()), param.getCode());
        String oldEmail = userInfo.getEmail();
        // email 置空需显式 set null(updateById 忽略 null 字段)
        userInfoManager.lambdaUpdate()
                .eq(MpIdEntity::getId, userInfo.getId())
                .set(UserInfo::getEmail, null)
                .set(UserInfo::isEmailVerified, false)
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
        // 清理可能存在的未完成绑定上下文, 防止解绑后 confirm 又绑上
        emailCodeService.delete(EmailCodeService.BIND_SCOPE, String.valueOf(userInfo.getId()));
        // 解绑成功通知旧邮箱(告知找回通道已切断, 非本人操作可及时察觉)
        Map<String, Object> noticeParams = Map.of("account", userInfo.getAccount());
        emailTemplateService.send(oldEmail, userInfo.getId(), EmailTemplateEnum.unbindNotice, noticeParams);
    }

    private UserInfo currentUser() {
        return userInfoManager.findById(SecurityUtil.getUserId())
                .orElseThrow(UserInfoNotExistsException::new);
    }

    /// 登录密码确认(RSA 解密 + BCrypt 比对 + 失败锁定 + 成功清零)
    private void verifyPassword(UserInfo userInfo, String encryptedPassword) {
        // 前置: 账号处于锁定状态时拒绝尝试(与登录锁定共用状态)
        loginRetryService.checkBeforeSensitiveVerify(userInfo.getId());
        String rawPassword = passwordDecryptService.decryptPassword(encryptedPassword);
        if (!BCrypt.checkpw(rawPassword, userInfo.getPassword())) {
            // 失败计数(REQUIRES_NEW 独立事务提交)
            loginRetryService.onSensitiveVerifyFailure(userInfo.getId(), userInfo.getAccount());
            // 权限: 登录密码错误
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.loginPasswordError");
        }
        // 验证通过, 清零失败计数(与登录成功同口径)
        loginRetryService.onSensitiveVerifySuccess(userInfo.getId());
    }
}
