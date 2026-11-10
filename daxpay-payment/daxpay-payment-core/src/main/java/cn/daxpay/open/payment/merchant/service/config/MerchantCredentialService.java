package cn.daxpay.open.payment.merchant.service.config;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import cn.daxpay.open.payment.merchant.convert.config.MerchantCredentialConvert;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户API配置服务（配置态）
///
/// 管理端/商户端凭证查询与更新。须在已有租户上下文（登录 Filter / 运营端 ignoreTable）下调用。
/// 网关验签取公钥请走 [cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCredentialService {
    private final MerchantCredentialManager credentialManager;
    private final MerchantInfoManager merchantInfoManager;
    private final PlatformConfigProperties platformConfigProperties;

    /// 根据商户号查询（配置态，租户内；运营端由 ignoreTable 放开）
    ///
    /// 无记录时自动建空行(有写副作用); 仅需判断配置状态的只读场景请直接用 manager 查询 + [#isConfigured]
    public MerchantCredentialResult findByMchNo(String mchNo) {
        // 展示用: 配置可能是 yml 引号跨行折成的单行(换行变空格)或面板注入的纯 Base64, 统一归一为标准 PEM
        String publicKey = RsaSignUtil.normalizePublicKeyPem(platformConfigProperties.getKeyConfig().getPublicKey());
        var credentialOptional = credentialManager.findByMchNo(mchNo);
        if (credentialOptional.isEmpty()){
            var merchant = merchantInfoManager.findByMchNo(mchNo)
                    // 商户: 商户不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.merchant.merchantNotExist"));
            var credential = new MerchantCredential();
            credential.setMchNo(mchNo);
            credentialManager.save(credential);
            return this.buildResult(credential, publicKey);
        }
        return this.buildResult(credentialOptional.get(), publicKey);
    }

    /// 更新
    public void update(MerchantCredentialParam param) {
        var credential = credentialManager.findByMchNo(param.getMchNo())
                // 商户: API配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.apiConfigNotExist"));
        // 凭据留空=保留原值: 空串归一为 null, 配合 Convert 的 null-IGNORE 跳过覆盖
        if (StrUtil.isBlank(param.getPublicKey())) {
            param.setPublicKey(null);
        }
        // 判断公钥是否合法
        if (StrUtil.isNotBlank(param.getPublicKey())){
            RsaSignUtil.loadPublicKeyFromPem(param.getPublicKey());
        }
        MerchantCredentialConvert.CONVERT.copy(param, credential);
        credentialManager.updateById(credential);
    }

    /// 凭证是否已配置完整: 记录存在且商户公钥、通信密钥均非空
    ///
    /// 凭证配置页状态、运营端商户工作台磁贴、配置检查器 `MchCredentialChecker` 共用此口径, 防漂移
    public static boolean isConfigured(MerchantCredential credential) {
        return isPublicKeyConfigured(credential) && isSecretKeyConfigured(credential);
    }

    /// 商户公钥是否已配置(记录不存在视为未配置)
    public static boolean isPublicKeyConfigured(MerchantCredential credential) {
        return credential != null && StrUtil.isNotBlank(credential.getPublicKey());
    }

    /// 通信密钥是否已配置(记录不存在视为未配置)
    public static boolean isSecretKeyConfigured(MerchantCredential credential) {
        return credential != null && StrUtil.isNotBlank(credential.getSecretKey());
    }

    /// 组装查询结果: 实体同名字段(含 lastModifiedTime)经 Convert 映射, 再补平台公钥与配置状态
    private MerchantCredentialResult buildResult(MerchantCredential credential, String platformPublicKey) {
        return credential.toResult()
                .setPlatformPublicKey(platformPublicKey)
                .setPublicKeyConfigured(isPublicKeyConfigured(credential))
                .setSecretKeyConfigured(isSecretKeyConfigured(credential))
                .setCredentialConfigured(isConfigured(credential));
    }
}
