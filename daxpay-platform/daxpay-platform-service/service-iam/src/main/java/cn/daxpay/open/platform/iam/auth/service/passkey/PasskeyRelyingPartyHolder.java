package cn.daxpay.open.platform.iam.auth.service.passkey;

import java.util.Set;
import java.util.stream.Collectors;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformWebAuthnConfig;
import cn.hutool.core.util.StrUtil;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # RelyingParty 实例持有器
///
/// RelyingParty 不可变, 而 RP 配置(rpId/origins)存于平台配置表可热修改;
/// 以配置内容指纹做缓存, 配置变更时重建实例, 避免每次请求重建。
/// 配置不完整(rpId/origins 缺失)时 fail-fast 抛出配置异常。
///
@Slf4j
@Component
@RequiredArgsConstructor
public class PasskeyRelyingPartyHolder {

    private final IamSecurityConfigService iamSecurityConfigService;

    private final PasskeyCredentialRepository credentialRepository;

    private volatile RelyingParty relyingParty;

    private volatile String fingerprint;

    /// 获取当前配置对应的 RelyingParty 实例
    public RelyingParty get() {
        PlatformWebAuthnConfig config = iamSecurityConfigService.getWebAuthnConfig();
        String fingerprint = fingerprint(config);
        RelyingParty current = this.relyingParty;
        if (current == null || !fingerprint.equals(this.fingerprint)) {
            synchronized (this) {
                if (this.relyingParty == null || !fingerprint.equals(this.fingerprint)) {
                    this.relyingParty = build(config);
                    this.fingerprint = fingerprint;
                    // 通行密钥 RP 配置变更, 重建 RelyingParty 实例
                    log.info("WebAuthn RelyingParty 已重建, rpId: {}", config.getRpId());
                }
            }
            current = this.relyingParty;
        }
        return current;
    }

    /// 按配置构建 RelyingParty
    private RelyingParty build(PlatformWebAuthnConfig config) {
        if (!Boolean.TRUE.equals(config.getEnabled())) {
            throw new BizInfoException("error.iam.passkey.platformDisabled");
        }
        if (StrUtil.isBlank(config.getRpId())) {
            throw new BizInfoException("error.iam.passkey.configIncomplete");
        }
        Set<String> origins = config.getOrigins() == null ? Set.of()
                : config.getOrigins().stream()
                    .filter(StrUtil::isNotBlank)
                    .map(String::trim)
                    .collect(Collectors.toSet());
        if (origins.isEmpty()) {
            throw new BizInfoException("error.iam.passkey.configIncomplete");
        }
        String rpName = StrUtil.blankToDefault(config.getRpName(), PlatformWebAuthnConfig.DEFAULT_RP_NAME);
        return RelyingParty.builder()
            .identity(RelyingPartyIdentity.builder()
                .id(config.getRpId())
                .name(rpName)
                .build())
            .credentialRepository(credentialRepository)
            .origins(origins)
            // attestation=none 场景不收集证明也未配信任源, 须放行未信任 attestation(库默认即 true);
            // 若设 false, finishRegistration 会因无法推导信任链必抛
            // "Failed to derive trust for attestation key."
            .allowUntrustedAttestation(true)
            .build();
    }

    /// 配置内容指纹(变更即重建)
    private String fingerprint(PlatformWebAuthnConfig config) {
        return config.getEnabled() + "|" + config.getRpId() + "|" + config.getRpName() + "|"
                + (config.getOrigins() == null ? "" : String.join(",", config.getOrigins()));
    }
}
