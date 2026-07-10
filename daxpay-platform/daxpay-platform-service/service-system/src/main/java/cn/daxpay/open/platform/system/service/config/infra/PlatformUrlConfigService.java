package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.system.convert.config.infra.PlatformUrlConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformUrlConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformUrlConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台端点配置服务
///
/// 管理系统访问地址等端点配置
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformUrlConfigService {

    private final SystemPlatformConfigService systemConfigService;

    /// 获取端点配置
    public PlatformUrlConfig getUrlConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.URL,
                PlatformUrlConfig.class,
                new PlatformUrlConfig());
    }

    /// 获取端点配置
    public PlatformUrlConfigResult findUrlConfig() {
        return PlatformUrlConfigConvert.CONVERT.toUrlResult(this.getUrlConfig());
    }

    /// 更新端点配置
    public void updateUrlConfig(PlatformUrlConfigParam param) {
        PlatformUrlConfig data = this.getUrlConfig();
        PlatformUrlConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.URL, data);
    }
}
