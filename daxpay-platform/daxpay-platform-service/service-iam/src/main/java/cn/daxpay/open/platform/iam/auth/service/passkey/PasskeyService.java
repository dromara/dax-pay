package cn.daxpay.open.platform.iam.auth.service.passkey;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import cn.daxpay.open.platform.capability.auth.code.AuthLoginTypeCode;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.TokenService;
import cn.daxpay.open.platform.iam.dao.passkey.UserPasskeyManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.passkey.UserPasskey;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.platform.iam.exception.user.UserInfoNotExistsException;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyDeleteParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyLoginVerifyParam;
import cn.daxpay.open.platform.iam.param.passkey.PasskeyRegisterParam;
import cn.daxpay.open.platform.iam.result.passkey.PasskeyLoginOptionsResult;
import cn.daxpay.open.platform.iam.result.passkey.PasskeyRegisterOptionsResult;
import cn.daxpay.open.platform.iam.result.passkey.UserPasskeyResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformWebAuthnConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.AttestationConveyancePreference;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.AuthenticatorSelectionCriteria;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialParameters;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.ResidentKeyRequirement;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.data.UserVerificationRequirement;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 通行密钥服务
///
/// 编排 WebAuthn 注册/登录两阶段交互与凭据生命周期管理。
/// 登录验证通过后与社交登录同模式, 走 [TokenService#completeAuthenticatedLogin]
/// 统一收尾(账号状态检查/会话/并发策略/登录日志), 2FA 豁免由登录类型 passkey 驱动。
///
/// userHandle 约定为 userId 的 8 字节大端表示, 是 discoverable 登录的用户寻址键。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PasskeyService {

    /// challenge 有效期(毫秒, 与下发 options.timeout 一致)
    private static final long TIMEOUT_MILLIS = 300_000L;

    /// challenge 随机字节数
    private static final int CHALLENGE_BYTES = 32;

    /// 注册支持的公钥算法(ES256 平台认证器全支持, RS256 Windows Hello 兼容)
    private static final List<PublicKeyCredentialParameters> PUB_KEY_CRED_PARAMS = List.of(
            PublicKeyCredentialParameters.ES256,
            PublicKeyCredentialParameters.RS256);

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserPasskeyManager userPasskeyManager;

    private final UserInfoManager userInfoManager;

    private final UserQueryService userQueryService;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final PasskeyChallengeService challengeService;

    private final PasskeyRelyingPartyHolder relyingPartyHolder;

    private final PasswordDecryptService passwordDecryptService;

    private final TokenService tokenService;

    private final LoginRetryService loginRetryService;

    private final PlatformStarterProperties platformStarterProperties;

    // ==================== 登录(匿名) ====================

    /// 生成登录认证选项(discoverable 免输账号, allowCredentials 为空)
    public PasskeyLoginOptionsResult loginOptions(String clientCode) {
        checkPlatformEnabled();
        ByteArray challenge = randomChallenge();
        String challengeId = challengeService.saveAuth(
                new PasskeyChallengeService.AuthContext(challenge.getBase64Url(), clientCode));
        return new PasskeyLoginOptionsResult()
                .setChallengeId(challengeId)
                .setOptions(new PasskeyLoginOptionsResult.RequestOptions()
                        .setChallenge(challenge.getBase64Url())
                        .setTimeout(TIMEOUT_MILLIS)
                        .setRpId(getWebAuthnConfig().getRpId())
                        .setAllowCredentials(List.of())
                        .setUserVerification(UserVerificationRequirement.REQUIRED.getValue()));
    }

    /// 验证登录断言, 通过后走统一登录收尾, 返回 token
    public String verifyLogin(PasskeyLoginVerifyParam param, HttpServletRequest request,
                              HttpServletResponse response) {
        PasskeyChallengeService.AuthContext context = challengeService.consumeAuth(param.getChallengeId());
        if (context == null) {
            throw new BizInfoException("error.iam.passkey.challengeExpired");
        }
        // 会话绑定的终端与本次请求须一致
        if (!context.clientCode().equals(param.getClient())) {
            throw new BizInfoException("error.iam.passkey.clientMismatch");
        }
        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential =
                parseAssertion(param.getCredentialJson());
        AssertionRequest assertionRequest = AssertionRequest.builder()
                .publicKeyCredentialRequestOptions(PublicKeyCredentialRequestOptions.builder()
                        .challenge(fromBase64Url(context.challenge()))
                        .rpId(getWebAuthnConfig().getRpId())
                        .timeout(TIMEOUT_MILLIS)
                        .userVerification(UserVerificationRequirement.REQUIRED)
                        .build())
                .build();
        AssertionResult result;
        try {
            result = relyingPartyHolder.get().finishAssertion(FinishAssertionOptions.builder()
                    .request(assertionRequest)
                    .response(credential)
                    .build());
        }
        catch (AssertionFailedException e) {
            log.warn("通行密钥登录断言验证失败: {}", e.getMessage());
            throw new BizInfoException("error.iam.passkey.assertionFailed");
        }
        if (!result.isSuccess() || !result.isUserVerified()) {
            throw new BizInfoException("error.iam.passkey.assertionFailed");
        }
        // userHandle → userId, 并校验账号归属端与请求终端一致
        Long userId = userHandleToId(result.getCredential().getUserHandle())
                .orElseThrow(() -> new BizInfoException("error.iam.passkey.assertionFailed"));
        UserInfoResult userInfo = userQueryService.findById(userId);
        if (!userInfo.getClientCode().equals(param.getClient())) {
            throw new BizInfoException("error.iam.passkey.clientMismatch");
        }
        // 更新凭据使用状态(签名计数防克隆 + 最后使用时间)
        updateCredentialUsage(credential.getId().getBase64Url(), result.getSignatureCount());
        // 走统一登录收尾(与 [IamSocialLoginHandler] 同模式)
        UserDetail userDetail = userInfo.toUserDetail();
        if (!userDetail.isAdmin()) {
            loginRetryService.setPasswordStatusToUserDetail(userDetail);
        }
        AuthInfoResult authInfoResult = new AuthInfoResult()
                .setId(userId)
                .setClient(param.getClient())
                .setLoginType(AuthLoginTypeCode.PASSKEY)
                .setUserDetail(userDetail);
        LoginAuthContext loginContext = new LoginAuthContext()
                .setRequest(request)
                .setResponse(response)
                .setClientCode(param.getClient())
                .setAuthLoginType(AuthLoginTypeCode.PASSKEY)
                .setAuthProperties(platformStarterProperties.getAuth())
                .setUserDetail(userDetail);
        return tokenService.completeAuthenticatedLogin(authInfoResult, loginContext);
    }

    // ==================== 注册(需登录) ====================

    /// 生成注册选项, 前置登录密码确认(防借用会话植入凭据)
    public PasskeyRegisterOptionsResult registerOptions(String encryptedPassword) {
        checkPlatformEnabled();
        Long userId = SecurityUtil.getUserId();
        UserInfo userInfo = userInfoManager.findById(userId)
                .orElseThrow(UserInfoNotExistsException::new);
        verifyPassword(userInfo, encryptedPassword);
        ByteArray challenge = randomChallenge();
        String challengeId = challengeService.saveRegister(new PasskeyChallengeService.RegisterContext(
                challenge.getBase64Url(), userId, userInfo.getClientCode(), userInfo.getAccount(), userInfo.getName()));
        PlatformWebAuthnConfig config = getWebAuthnConfig();
        // 排除该用户已有凭据, 防止同一认证器重复注册
        List<PasskeyRegisterOptionsResult.CredentialDescriptor> excludes = userPasskeyManager.findByUserId(userId).stream()
                .map(passkey -> new PasskeyRegisterOptionsResult.CredentialDescriptor()
                        .setId(passkey.getCredentialId())
                        .setType(PublicKeyCredentialType.PUBLIC_KEY.getId()))
                .toList();
        PasskeyRegisterOptionsResult.CreationOptions options = new PasskeyRegisterOptionsResult.CreationOptions()
                .setRp(new PasskeyRegisterOptionsResult.Rp()
                        .setId(config.getRpId())
                        .setName(rpName(config)))
                .setUser(new PasskeyRegisterOptionsResult.User()
                        .setId(userIdToHandle(userId).getBase64Url())
                        .setName(userInfo.getAccount())
                        .setDisplayName(userInfo.getName()))
                .setChallenge(challenge.getBase64Url())
                .setPubKeyCredParams(pubKeyCredParams())
                .setTimeout(TIMEOUT_MILLIS)
                .setExcludeCredentials(excludes)
                .setAuthenticatorSelection(new PasskeyRegisterOptionsResult.AuthenticatorSelection()
                        .setResidentKey(ResidentKeyRequirement.REQUIRED.getValue())
                        .setUserVerification(UserVerificationRequirement.REQUIRED.getValue()))
                .setAttestation(AttestationConveyancePreference.NONE.getValue());
        return new PasskeyRegisterOptionsResult()
                .setChallengeId(challengeId)
                .setOptions(options);
    }

    /// 完成注册: 验证 attestation 并落库凭据
    @Transactional(rollbackFor = Exception.class)
    public UserPasskeyResult register(PasskeyRegisterParam param) {
        PasskeyChallengeService.RegisterContext context = challengeService.consumeRegister(param.getChallengeId());
        if (context == null) {
            throw new BizInfoException("error.iam.passkey.challengeExpired");
        }
        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> credential =
                parseRegistration(param.getCredentialJson());
        RegistrationResult result;
        try {
            result = relyingPartyHolder.get().finishRegistration(FinishRegistrationOptions.builder()
                    .request(buildCreationOptions(context))
                    .response(credential)
                    .build());
        }
        catch (RegistrationFailedException e) {
            log.warn("通行密钥注册验证失败: {}", e.getMessage());
            throw new BizInfoException("error.iam.passkey.registrationFailed");
        }
        String credentialId = result.getKeyId().getId().getBase64Url();
        // 凭据全局唯一, 已被其他账号绑定时拒绝
        if (userPasskeyManager.findByCredentialId(credentialId).isPresent()) {
            throw new BizInfoException("error.iam.passkey.credentialExists");
        }
        UserPasskey entity = new UserPasskey()
                .setUserId(context.userId())
                .setClientCode(context.clientCode())
                .setCredentialId(credentialId)
                .setPublicKey(result.getPublicKeyCose().getBase64Url())
                .setSignCount(result.getSignatureCount())
                .setDeviceName(param.getDeviceName())
                .setTransports(param.getTransports() == null ? null : String.join(",", param.getTransports()))
                .setBackupEligible(result.isBackupEligible())
                .setBackupState(result.isBackedUp());
        userPasskeyManager.save(entity);
        return toResult(entity);
    }

    // ==================== 凭据管理(需登录) ====================

    /// 当前用户已绑定的通行密钥列表
    public List<UserPasskeyResult> list() {
        return userPasskeyManager.findByUserId(SecurityUtil.getUserId()).stream()
                .map(this::toResult)
                .toList();
    }

    /// 重命名凭据(仅限本人凭据)
    public void rename(Long id, String deviceName) {
        UserPasskey entity = userPasskeyManager.findByIdAndUserId(id, SecurityUtil.getUserId())
                .orElseThrow(() -> new BizInfoException("error.iam.passkey.notExists"));
        entity.setDeviceName(deviceName);
        userPasskeyManager.updateById(entity);
    }

    /// 删除凭据(仅限本人凭据, 前置登录密码确认)
    public void delete(Long id, String encryptedPassword) {
        Long userId = SecurityUtil.getUserId();
        UserInfo userInfo = userInfoManager.findById(userId)
                .orElseThrow(UserInfoNotExistsException::new);
        verifyPassword(userInfo, encryptedPassword);
        UserPasskey entity = userPasskeyManager.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BizInfoException("error.iam.passkey.notExists"));
        userPasskeyManager.deleteById(entity.getId());
    }

    // ==================== 内部方法 ====================

    /// 校验平台是否启用通行密钥认证
    private void checkPlatformEnabled() {
        if (!Boolean.TRUE.equals(getWebAuthnConfig().getEnabled())) {
            throw new BizInfoException("error.iam.passkey.platformDisabled");
        }
    }

    private PlatformWebAuthnConfig getWebAuthnConfig() {
        return iamSecurityConfigService.getWebAuthnConfig();
    }

    /// 站点显示名称(兜底默认值)
    private String rpName(PlatformWebAuthnConfig config) {
        return StrUtil.blankToDefault(config.getRpName(), PlatformWebAuthnConfig.DEFAULT_RP_NAME);
    }

    /// 登录密码确认(RSA 解密 + BCrypt 比对 + 失败锁定 + 成功清零)
    private void verifyPassword(UserInfo userInfo, String encryptedPassword) {
        // 前置: 账号处于锁定状态时拒绝尝试(与登录锁定共用状态)
        loginRetryService.checkBeforeSensitiveVerify(userInfo.getId());
        String rawPassword = passwordDecryptService.decryptPassword(encryptedPassword);
        if (!BCrypt.checkpw(rawPassword, userInfo.getPassword())) {
            // 失败计数(REQUIRES_NEW 独立事务提交)
            loginRetryService.onSensitiveVerifyFailure(userInfo.getId(), userInfo.getAccount());
            throw new BizException(CommonCode.FAIL_CODE, "error.iam.user.loginPasswordError");
        }
        // 验证通过, 清零失败计数(与登录成功同口径)
        loginRetryService.onSensitiveVerifySuccess(userInfo.getId());
    }

    /// 解析认证器断言响应 JSON
    private PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> parseAssertion(String json) {
        try {
            return PublicKeyCredential.parseAssertionResponseJson(json);
        }
        catch (IOException e) {
            throw new BizInfoException("error.iam.passkey.invalidCredential");
        }
    }

    /// 解析认证器注册响应 JSON
    private PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> parseRegistration(String json) {
        try {
            return PublicKeyCredential.parseRegistrationResponseJson(json);
        }
        catch (IOException e) {
            throw new BizInfoException("error.iam.passkey.invalidCredential");
        }
    }

    /// 重建注册请求选项(与下发内容一致, 供 finishRegistration 校验)
    private PublicKeyCredentialCreationOptions buildCreationOptions(PasskeyChallengeService.RegisterContext context) {
        PlatformWebAuthnConfig config = getWebAuthnConfig();
        Set<PublicKeyCredentialDescriptor> excludes = userPasskeyManager.findByUserId(context.userId()).stream()
                .map(passkey -> PublicKeyCredentialDescriptor.builder()
                        .id(fromBase64Url(passkey.getCredentialId()))
                        .build())
                .collect(Collectors.toSet());
        return PublicKeyCredentialCreationOptions.builder()
                .rp(RelyingPartyIdentity.builder()
                        .id(config.getRpId())
                        .name(rpName(config))
                        .build())
                .user(UserIdentity.builder()
                        .name(context.account())
                        .displayName(context.name())
                        .id(userIdToHandle(context.userId()))
                        .build())
                .challenge(fromBase64Url(context.challenge()))
                .pubKeyCredParams(PUB_KEY_CRED_PARAMS)
                .timeout(TIMEOUT_MILLIS)
                .excludeCredentials(excludes)
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                        .residentKey(ResidentKeyRequirement.REQUIRED)
                        .userVerification(UserVerificationRequirement.REQUIRED)
                        .build())
                .attestation(AttestationConveyancePreference.NONE)
                .build();
    }

    /// 更新凭据使用状态(乐观锁独立更新对象, 不覆盖其他字段)
    private void updateCredentialUsage(String credentialId, long signatureCount) {
        userPasskeyManager.findByCredentialId(credentialId).ifPresent(entity -> {
            UserPasskey update = new UserPasskey();
            update.setId(entity.getId());
            update.setVersion(entity.getVersion());
            update.setSignCount(signatureCount);
            update.setLastUsedTime(OffsetDateTime.now(ZoneOffset.UTC));
            userPasskeyManager.updateById(update);
        });
    }

    /// 生成随机 challenge 值(256bit, 一次性防重放)
    private ByteArray randomChallenge() {
        byte[] bytes = new byte[CHALLENGE_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return new ByteArray(bytes);
    }

    /// 前端注册选项的公钥算法参数列表
    private List<PasskeyRegisterOptionsResult.PubKeyCredParam> pubKeyCredParams() {
        return PUB_KEY_CRED_PARAMS.stream()
                .map(param -> new PasskeyRegisterOptionsResult.PubKeyCredParam()
                        // getType().toString() 是大写枚举名 PUBLIC_KEY, W3C/浏览器要求小写 public-key,
                        // 必须用 getId() 取标准值, 否则浏览器报 No entry in pubKeyCredParams was of type "public-key"
                        .setType(param.getType().getId())
                        .setAlg((int) param.getAlg().getId()))
                .toList();
    }

    /// base64url 解码(库方法抛受检异常, 此处数据源为库自身编码的值, 失败视为非法输入)
    public static ByteArray fromBase64Url(String base64Url) {
        try {
            return ByteArray.fromBase64Url(base64Url);
        }
        catch (com.yubico.webauthn.data.exception.Base64UrlException e) {
            throw new BizInfoException("error.iam.passkey.invalidCredential");
        }
    }

    private UserPasskeyResult toResult(UserPasskey entity) {
        return new UserPasskeyResult()
                .setId(entity.getId())
                .setDeviceName(entity.getDeviceName())
                .setTransports(entity.getTransports())
                .setBackupEligible(entity.getBackupEligible())
                .setBackupState(entity.getBackupState())
                .setCreateTime(entity.getCreateTime())
                .setLastUsedTime(entity.getLastUsedTime());
    }

    /// userId 转 userHandle(8 字节大端)
    public static ByteArray userIdToHandle(Long userId) {
        long value = userId;
        return new ByteArray(new byte[] {
                (byte) (value >> 56), (byte) (value >> 48), (byte) (value >> 40), (byte) (value >> 32),
                (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value });
    }

    /// userHandle 转 userId(8 字节大端), 长度不符返回空
    public static Optional<Long> userHandleToId(ByteArray userHandle) {
        if (userHandle == null || userHandle.size() != 8) {
            return Optional.empty();
        }
        byte[] bytes = userHandle.getBytes();
        long value = 0;
        for (byte b : bytes) {
            value = (value << 8) | (b & 0xFF);
        }
        return Optional.of(value);
    }
}
