package cn.daxpay.open.platform.system.service.config.security;

import cn.daxpay.open.platform.system.convert.config.security.PlatformSocialAutoLoginConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSocialAutoLoginConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.security.PlatformSocialAutoLoginConfigParam;
import cn.daxpay.open.platform.system.result.config.security.PlatformSocialAutoLoginConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// # 应用内社交自动登录配置服务
///
/// 仅负责读写 [PlatformConfigTypeEnum#SECURITY_SOCIAL_AUTO];
/// 社交平台是否已启用等业务校验由 IAM 层在更新前完成.
///
@Service
@RequiredArgsConstructor
public class PlatformSocialAutoLoginConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// 获取配置实体(不存在则创建默认关闭态), 并归一化旧 source 字段
    public PlatformSocialAutoLoginConfig getConfig() {
        PlatformSocialAutoLoginConfig config = systemConfigService.getOrCreateConfig(
                PlatformConfigTypeEnum.SECURITY_SOCIAL_AUTO,
                PlatformSocialAutoLoginConfig.class,
                defaultConfig());
        this.normalize(config);
        return config;
    }

    /// 查询配置结果
    public PlatformSocialAutoLoginConfigResult findConfig() {
        return PlatformSocialAutoLoginConfigConvert.CONVERT.toResult(this.getConfig());
    }

    /// 更新配置(调用方已完成业务校验)
    public void updateConfig(PlatformSocialAutoLoginConfigParam param) {
        PlatformSocialAutoLoginConfig data = this.getConfig();
        PlatformSocialAutoLoginConfigConvert.CONVERT.copy(param, data);
        // 嵌套对象 MapStruct 可能留下 null, 兜底为空对象避免 NPE
        if (data.getAdmin() == null) {
            data.setAdmin(new PlatformSocialAutoLoginConfig.ClientAutoLogin());
        }
        if (data.getMerchant() == null) {
            data.setMerchant(new PlatformSocialAutoLoginConfig.ClientAutoLogin());
        }
        // 参数可能仍带旧 source, 统一合并后落盘只保留 sources
        this.applyParamSources(data.getAdmin(), param.getAdmin());
        this.applyParamSources(data.getMerchant(), param.getMerchant());
        this.normalize(data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.SECURITY_SOCIAL_AUTO, data);
    }

    /// 默认: 两端均关闭
    private PlatformSocialAutoLoginConfig defaultConfig() {
        return new PlatformSocialAutoLoginConfig()
                .setAdmin(new PlatformSocialAutoLoginConfig.ClientAutoLogin().setEnabled(false))
                .setMerchant(new PlatformSocialAutoLoginConfig.ClientAutoLogin().setEnabled(false));
    }

    private void normalize(PlatformSocialAutoLoginConfig config) {
        if (config.getAdmin() != null) {
            config.getAdmin().normalize();
        }
        if (config.getMerchant() != null) {
            config.getMerchant().normalize();
        }
    }

    /// 将 Param 的 sources/source 写回实体, 避免 MapStruct 只拷同名后遗留旧 source
    private void applyParamSources(PlatformSocialAutoLoginConfig.ClientAutoLogin target,
                                   PlatformSocialAutoLoginConfigParam.ClientAutoLoginParam param) {
        if (target == null || param == null) {
            return;
        }
        List<String> resolved = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getSources())) {
            for (String item : param.getSources()) {
                if (StrUtil.isNotBlank(item) && !resolved.contains(item)) {
                    resolved.add(item);
                }
            }
        } else if (StrUtil.isNotBlank(param.getSource())) {
            resolved.add(param.getSource());
        }
        target.setSources(resolved);
        target.setSource(null);
    }
}
