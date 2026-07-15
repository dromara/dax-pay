package cn.daxpay.open.plugin.easypay.service.config;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.plugin.easypay.convert.EasyPayCredentialConvert;
import cn.daxpay.open.plugin.easypay.dao.EasyPayConfigManager;
import cn.daxpay.open.plugin.easypay.dao.EasyPayCredentialManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayConfig;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayCredentialParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayCredentialResult;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易支付凭证服务（应用级）
///
@Service
@RequiredArgsConstructor
public class EasyPayCredentialService {

    private final EasyPayCredentialManager easyPayCredentialManager;
    private final EasyPayConfigManager easyPayConfigManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final PlatformConfigProperties platformConfigProperties;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final MerchantContextLoader merchantContextLoader;

    /// 按应用号查询（不存在则创建）
    public EasyPayCredentialResult findResultByAppId(String appId) {
        var credential = this.findByAppId(appId);
        var result = credential.toResult();
        var backend = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        // 尾部 / 便于客户端拼接 submit.php、api/pay/* 等路径
        result.setEasyPayV1ApiUrl(backend + "/epay/api/v1/")
                .setEasyPayV2ApiUrl(backend + "/epay/api/v2/");
        // 平台公钥展示为纯 Base64（去 PEM 头尾与空白），对接方直接复制可用
        result.setPlatformPublicKey(EasyPayUtil.stripPemPublicKey(result.getPlatformPublicKey()));
        return result;
    }

    /// 按应用号查询实体（不存在则创建空凭证）
    public EasyPayCredential findByAppId(String appId) {
        var keyConfig = platformConfigProperties.getKeyConfig();
        var optional = easyPayCredentialManager.findByAppId(appId);
        if (optional.isPresent()) {
            return optional.get()
                    .setPlatformPublicKey(keyConfig.getPublicKey())
                    .setPlatformPrivateKey(keyConfig.getPrivateKey());
        }
        var app = mchAppInfoManager.requireByAppId(appId);
        merchantContextLoader.initMch(app.getMchNo());
        var credential = new EasyPayCredential();
        credential.setPid(this.generatePid());
        credential.setAppId(appId);
        credential.setMchNo(app.getMchNo());
        credential.setEnable(false);
        credential.setEnableV1(false);
        credential.setEnableV2(true);
        credential.setUseSystemKey(true);
        easyPayCredentialManager.save(credential);
        // 同步创建配置壳
        if (easyPayConfigManager.findByAppId(appId).isEmpty()) {
            var config = new EasyPayConfig();
            config.setPid(credential.getPid());
            config.setAppId(appId);
            config.setMchNo(app.getMchNo());
            easyPayConfigManager.save(config);
        }
        credential.setPlatformPublicKey(keyConfig.getPublicKey());
        credential.setPlatformPrivateKey(keyConfig.getPrivateKey());
        return credential;
    }

    /// 更新凭证
    @Transactional(rollbackFor = Exception.class)
    public void update(EasyPayCredentialParam param) {
        var credential = this.findByAppId(param.getAppId());
        EasyPayCredentialConvert.CONVERT.copy(param, credential);
        easyPayCredentialManager.updateById(credential);
    }

    /// 按 pid 获取并校验启用
    public EasyPayCredential getAndCheck(Integer pid) {
        var credential = easyPayCredentialManager.findByPidNotTenant(pid)
                .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.credentialNotFound"));
        if (!credential.getEnable()) {
            throw new ValidationFailedException("error.plugin.easypay.credentialDisabled");
        }
        var keyConfig = platformConfigProperties.getKeyConfig();
        return credential
                .setPlatformPublicKey(keyConfig.getPublicKey())
                .setPlatformPrivateKey(keyConfig.getPrivateKey());
    }

    /// 生成唯一 pid
    private Integer generatePid() {
        for (int i = 0; i < 20; i++) {
            int pid = RandomUtil.randomInt(100000, Integer.MAX_VALUE);
            if (easyPayCredentialManager.findByPidNotTenant(pid).isEmpty()) {
                return pid;
            }
        }
        throw new ValidationFailedException("error.plugin.easypay.pidGenerateFailed");
    }
}
