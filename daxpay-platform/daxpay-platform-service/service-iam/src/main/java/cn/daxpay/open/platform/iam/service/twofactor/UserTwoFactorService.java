package cn.daxpay.open.platform.iam.service.twofactor;

import java.util.List;
import java.util.Optional;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.twofactor.BackupCodeEntry;
import cn.daxpay.open.platform.iam.auth.service.twofactor.BackupCodeService;
import cn.daxpay.open.platform.iam.auth.service.twofactor.TotpService;
import cn.daxpay.open.platform.iam.dao.twofactor.UserTwoFactorManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.twofactor.UserTwoFactor;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.result.twofactor.BackupCodeResult;
import cn.daxpay.open.platform.iam.result.twofactor.TwoFactorSetupResult;
import cn.daxpay.open.platform.iam.result.twofactor.TwoFactorStatusResult;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformTwoFactorAuthConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 用户双因素认证服务
///
/// 管理 TOTP 绑定生命周期(初始化/确认/关闭/重置备用码)与状态查询,
/// 同时为登录流程提供校验入口(是否已绑定、校验动态码、消费备用码)。
///
/// 数据模型: 记录存在即代表已启用, secret 使用 AES-256-GCM 加密存储,
/// backup_codes 以 jsonb 存储哈希条目, 明文仅生成时一次性返回。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTwoFactorService {

    private final UserTwoFactorManager userTwoFactorManager;

    private final TotpService totpService;

    private final BackupCodeService backupCodeService;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final UserInfoManager userInfoManager;

    /// 查询当前用户的双因素认证状态
    public TwoFactorStatusResult getStatus() {
        Long userId = SecurityUtil.getUserId();
        PlatformTwoFactorAuthConfig config = getConfig();
        TwoFactorStatusResult result = new TwoFactorStatusResult()
                .setPlatformEnabled(Boolean.TRUE.equals(config.getEnabled()));
        Optional<UserTwoFactor> bound = userTwoFactorManager.findByUserId(userId);
        if (bound.isPresent()) {
            UserTwoFactor entity = bound.get();
            result.setBound(true)
                    .setBackupCodesRemaining(entity.getBackupCodesRemaining());
        }
        else {
            result.setBound(false);
        }
        return result;
    }

    /// 初始化绑定: 生成密钥与 otpauth URI(不落库, 待确认)
    public TwoFactorSetupResult setup() {
        checkPlatformEnabled();
        Long userId = SecurityUtil.getUserId();
        // 已绑定不允许重复初始化
        if (userTwoFactorManager.findByUserId(userId).isPresent()) {
            // 双因素认证: 已绑定, 无需重复操作
            throw new OperationFailException("error.iam.twoFactor.alreadyBound");
        }
        String account = userInfoManager.findById(userId)
                .map(UserInfo::getAccount)
                .orElse(userId.toString());
        String secret = totpService.generateSecret();
        String otpAuthUri = totpService.buildOtpAuthUri(secret, account);
        return new TwoFactorSetupResult()
                .setSecret(secret)
                .setOtpAuthUri(otpAuthUri);
    }

    /// 确认绑定: 校验动态码, 通过则落库启用并生成备用码
    @Transactional(rollbackFor = Exception.class)
    public BackupCodeResult confirm(String secret, String code) {
        checkPlatformEnabled();
        Long userId = SecurityUtil.getUserId();
        // 防止重复绑定
        if (userTwoFactorManager.findByUserId(userId).isPresent()) {
            throw new OperationFailException("error.iam.twoFactor.alreadyBound");
        }
        // 校验动态码
        if (!totpService.verifyCode(secret, code)) {
            // 双因素认证: 动态码错误
            throw new OperationFailException("error.iam.twoFactor.codeError");
        }
        // 生成备用码
        int count = defaultBackupCodesCount();
        BackupCodeService.GeneratedBackupCode generated = backupCodeService.generate(count);
        // 落库
        UserTwoFactor entity = new UserTwoFactor()
                .setUserId(userId)
                .setSecret(secret)
                .setBackupCodes(JSONUtil.toJsonStr(generated.entries()))
                .setBackupCodesRemaining(count);
        userTwoFactorManager.save(entity);
        return new BackupCodeResult()
                .setCodes(generated.plaintextCodes())
                .setTotal(count);
    }

    /// 关闭双因素认证: 需校验动态码或备用码二次确认
    @Transactional(rollbackFor = Exception.class)
    public void disable(String code, String codeType) {
        Long userId = SecurityUtil.getUserId();
        UserTwoFactor entity = requireBound(userId);
        if (!verifyByCodeType(entity, code, codeType)) {
            throw new OperationFailException("error.iam.twoFactor.codeError");
        }
        userTwoFactorManager.deleteById(entity.getId());
    }

    /// 重新生成备用验证码: 需校验动态码或备用码, 旧备用码全部作废
    @Transactional(rollbackFor = Exception.class)
    public BackupCodeResult regenerateBackupCodes(String code, String codeType) {
        Long userId = SecurityUtil.getUserId();
        UserTwoFactor entity = requireBound(userId);
        if (!verifyByCodeType(entity, code, codeType)) {
            throw new OperationFailException("error.iam.twoFactor.codeError");
        }
        int count = defaultBackupCodesCount();
        BackupCodeService.GeneratedBackupCode generated = backupCodeService.generate(count);
        entity.setBackupCodes(JSONUtil.toJsonStr(generated.entries()))
                .setBackupCodesRemaining(count);
        userTwoFactorManager.updateById(entity);
        return new BackupCodeResult()
                .setCodes(generated.plaintextCodes())
                .setTotal(count);
    }

    // ==================== 登录流程接入接口(接收 userId, 不依赖当前会话) ====================

    /// 登录是否需要双因素认证: 平台已开启 且 用户已绑定
    public boolean isTwoFactorRequired(Long userId) {
        return Boolean.TRUE.equals(getConfig().getEnabled()) && isBound(userId);
    }

    /// 用户是否已绑定双因素认证
    public boolean isBound(Long userId) {
        return userTwoFactorManager.findByUserId(userId).isPresent();
    }

    /// 校验 TOTP 动态码(登录二次验证用)
    public boolean verifyTotpCode(Long userId, String code) {
        UserTwoFactor entity = userTwoFactorManager.findByUserId(userId).orElse(null);
        if (entity == null) {
            return false;
        }
        boolean valid = totpService.verifyCode(entity.getSecret(), code);
        return valid;
    }

    /// 校验并消费备用验证码(登录二次验证用, 通过 userId 查 entity)
    @Transactional(rollbackFor = Exception.class)
    public boolean consumeBackupCode(Long userId, String code) {
        UserTwoFactor entity = userTwoFactorManager.findByUserId(userId).orElse(null);
        if (entity == null) {
            return false;
        }
        return consumeBackupCodeInternal(entity, code);
    }

    /// 校验并消费备用验证码(内部方法, 已持有 entity, 命中即置 used)
    private boolean consumeBackupCodeInternal(UserTwoFactor entity, String code) {
        if (StrUtil.isBlank(code) || StrUtil.isBlank(entity.getBackupCodes())) {
            return false;
        }
        List<BackupCodeEntry> entries = JSONUtil.toList(entity.getBackupCodes(), BackupCodeEntry.class);
        String inputHash = backupCodeService.hash(code);
        boolean consumed = false;
        int remaining = entity.getBackupCodesRemaining() == null ? 0 : entity.getBackupCodesRemaining();
        for (BackupCodeEntry entry : entries) {
            if (!entry.isUsed() && inputHash.equals(entry.getHash())) {
                entry.setUsed(true);
                consumed = true;
                remaining = Math.max(0, remaining - 1);
                break;
            }
        }
        if (consumed) {
            entity.setBackupCodes(JSONUtil.toJsonStr(entries))
                    .setBackupCodesRemaining(remaining);
            userTwoFactorManager.updateById(entity);
        }
        return consumed;
    }

    /// 按验证码类型校验: BACKUP 校验并消费备用码, 其他校验 TOTP 动态码
    private boolean verifyByCodeType(UserTwoFactor entity, String code, String codeType) {
        if ("BACKUP".equalsIgnoreCase(codeType)) {
            return consumeBackupCodeInternal(entity, code);
        }
        return totpService.verifyCode(entity.getSecret(), code);
    }

    // ==================== 内部方法 ====================

    /// 校验平台是否启用双因素认证
    private void checkPlatformEnabled() {
        if (!Boolean.TRUE.equals(getConfig().getEnabled())) {
            // 双因素认证: 平台未启用
            throw new OperationFailException("error.iam.twoFactor.platformDisabled");
        }
    }

    /// 获取已绑定记录, 不存在抛异常
    private UserTwoFactor requireBound(Long userId) {
        return userTwoFactorManager.findByUserId(userId)
                .orElseThrow(() -> new OperationFailException("error.iam.twoFactor.notBound"));
    }

    /// 默认备用码数量(从平台配置读取, 兜底 10)
    private int defaultBackupCodesCount() {
        Integer count = getConfig().getBackupCodesCount();
        return count == null || count <= 0 ? 10 : count;
    }

    private PlatformTwoFactorAuthConfig getConfig() {
        return iamSecurityConfigService.getTwoFactorAuthConfig();
    }
}
