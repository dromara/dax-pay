package cn.daxpay.open.platform.iam.auth.service.passkey;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import cn.daxpay.open.platform.iam.dao.passkey.UserPasskeyManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.passkey.UserPasskey;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 通行密钥凭据仓储适配层
///
/// 桥接 [com.yubico.webauthn.RelyingParty] 与 iam_user_passkey 表:
/// userHandle 约定为 userId 的 8 字节大端表示(见 [PasskeyService#userIdToHandle]);
/// username 约定使用账号(account), discoverable 登录流程实际只依赖 userHandle 路径。
///
@Service
@RequiredArgsConstructor
public class PasskeyCredentialRepository implements CredentialRepository {

    private final UserPasskeyManager userPasskeyManager;

    private final UserInfoManager userInfoManager;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        Set<PublicKeyCredentialDescriptor> descriptors = new HashSet<>();
        userInfoManager.findByAccount(username).ifPresent(user -> {
            for (UserPasskey passkey : userPasskeyManager.findByUserId(user.getId())) {
                descriptors.add(toDescriptor(passkey));
            }
        });
        return descriptors;
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return userInfoManager.findByAccount(username)
            .map(user -> PasskeyService.userIdToHandle(user.getId()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return PasskeyService.userHandleToId(userHandle)
            .flatMap(id -> userInfoManager.findById(id).map(UserInfo::getAccount));
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return lookupAll(credentialId).stream()
            .filter(credential -> credential.getUserHandle().equals(userHandle))
            .findFirst();
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        List<UserPasskey> passkeys = userPasskeyManager.findByCredentialId(credentialId.getBase64Url())
            .map(List::of)
            .orElse(List.of());
        Set<RegisteredCredential> credentials = new HashSet<>();
        for (UserPasskey passkey : passkeys) {
            long signatureCount = passkey.getSignCount() == null ? 0 : passkey.getSignCount();
            credentials.add(RegisteredCredential.builder()
                .credentialId(PasskeyService.fromBase64Url(passkey.getCredentialId()))
                .userHandle(PasskeyService.userIdToHandle(passkey.getUserId()))
                .publicKeyCose(PasskeyService.fromBase64Url(passkey.getPublicKey()))
                .signatureCount(signatureCount)
                .build());
        }
        return credentials;
    }

    /// 实体转凭据描述符
    private PublicKeyCredentialDescriptor toDescriptor(UserPasskey passkey) {
        return PublicKeyCredentialDescriptor.builder()
            .id(PasskeyService.fromBase64Url(passkey.getCredentialId()))
            .build();
    }
}
