package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSocialAutoLoginConfig;
import cn.daxpay.open.platform.system.param.config.security.PlatformSocialAutoLoginConfigParam;
import cn.daxpay.open.platform.system.result.config.security.PlatformSocialAutoLoginConfigResult;
import cn.daxpay.open.platform.system.service.config.security.PlatformSocialAutoLoginConfigService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/// # 应用内社交自动登录编排服务
///
/// 在平台配置读写之上叠加社交平台启用校验, 供运营配置页与登录上下文使用.
///
@Service
@RequiredArgsConstructor
public class SocialAutoLoginConfigService {

    /// 支持应用内 UA 静默授权的平台(不含开放平台扫码 weChatOpen)
    private static final Set<String> AUTO_LOGIN_ALLOWED_SOURCES = Set.of(
            SocialSourceEnum.FEISHU.getCode(),
            SocialSourceEnum.WECHAT_MP.getCode(),
            SocialSourceEnum.WE_COM.getCode()
    );

    private final PlatformSocialAutoLoginConfigService platformSocialAutoLoginConfigService;

    private final SocialLoginConfigService socialLoginConfigService;

    /// 查询完整配置(运营端管理)
    public PlatformSocialAutoLoginConfigResult findConfig() {
        return platformSocialAutoLoginConfigService.findConfig();
    }

    /// 更新配置: 开启时校验 sources 合法且社交登录平台已启用
    public void updateConfig(PlatformSocialAutoLoginConfigParam param) {
        this.validateClientItem(ClientEnum.ADMIN.getCode(), param.getAdmin());
        this.validateClientItem(ClientEnum.MERCHANT.getCode(), param.getMerchant());
        platformSocialAutoLoginConfigService.updateConfig(param);
    }

    /// 按终端解析对外下发的自动登录片段(登录页用)
    /// 过滤未启用或不可应用内自动登录的平台; 过滤后为空则视为未开启.
    public PlatformSocialAutoLoginConfig.ClientAutoLogin resolveForClient(String clientCode) {
        PlatformSocialAutoLoginConfig config = platformSocialAutoLoginConfigService.getConfig();
        PlatformSocialAutoLoginConfig.ClientAutoLogin item = this.pickClient(config, clientCode);
        if (item == null || !Boolean.TRUE.equals(item.getEnabled())) {
            return new PlatformSocialAutoLoginConfig.ClientAutoLogin().setEnabled(false);
        }
        List<String> configured = item.resolveSources();
        if (CollUtil.isEmpty(configured)) {
            return new PlatformSocialAutoLoginConfig.ClientAutoLogin().setEnabled(false);
        }
        List<String> effective = new ArrayList<>();
        for (String source : configured) {
            if (!AUTO_LOGIN_ALLOWED_SOURCES.contains(source)) {
                continue;
            }
            SocialLoginConfig enabled = socialLoginConfigService.findEnabledBySource(source);
            if (enabled != null) {
                effective.add(source);
            }
        }
        if (effective.isEmpty()) {
            return new PlatformSocialAutoLoginConfig.ClientAutoLogin().setEnabled(false);
        }
        return new PlatformSocialAutoLoginConfig.ClientAutoLogin()
                .setEnabled(true)
                .setSources(effective);
    }

    /// 校验单端配置项
    private void validateClientItem(String clientCode, PlatformSocialAutoLoginConfigParam.ClientAutoLoginParam item) {
        if (item == null || !Boolean.TRUE.equals(item.getEnabled())) {
            return;
        }
        List<String> sources = this.resolveParamSources(item);
        if (CollUtil.isEmpty(sources)) {
            // 开启自动登录时必须至少选择一个平台
            throw new OperationFailException("error.social.autoLogin.sourceRequired", clientCode);
        }
        for (String code : sources) {
            if (!AUTO_LOGIN_ALLOWED_SOURCES.contains(code)) {
                // 所选平台不支持应用内自动登录(如微信开放平台扫码)
                throw new OperationFailException("error.social.autoLogin.sourceNotAllowed", code);
            }
            SocialSourceEnum source = SocialSourceEnum.of(code);
            if (source == null) {
                throw new OperationFailException("error.social.unsupportedSource");
            }
            SocialLoginConfig enabled = socialLoginConfigService.findEnabledBySource(code);
            if (enabled == null) {
                // 所选平台未配置或未启用
                throw new OperationFailException("error.social.autoLogin.sourceNotEnabled", code);
            }
        }
    }

    private List<String> resolveParamSources(PlatformSocialAutoLoginConfigParam.ClientAutoLoginParam item) {
        List<String> resolved = new ArrayList<>();
        if (CollUtil.isNotEmpty(item.getSources())) {
            for (String code : item.getSources()) {
                if (StrUtil.isNotBlank(code) && !resolved.contains(code)) {
                    resolved.add(code);
                }
            }
        } else if (StrUtil.isNotBlank(item.getSource())) {
            resolved.add(item.getSource());
        }
        return resolved;
    }

    private PlatformSocialAutoLoginConfig.ClientAutoLogin pickClient(PlatformSocialAutoLoginConfig config,
                                                                     String clientCode) {
        if (ClientEnum.ADMIN.getCode().equals(clientCode)) {
            return config.getAdmin();
        }
        if (ClientEnum.MERCHANT.getCode().equals(clientCode)) {
            return config.getMerchant();
        }
        return null;
    }
}
