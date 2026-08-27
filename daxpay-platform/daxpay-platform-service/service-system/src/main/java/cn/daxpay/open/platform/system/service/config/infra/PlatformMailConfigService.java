package cn.daxpay.open.platform.system.service.config.infra;

import cn.daxpay.open.platform.system.convert.config.infra.PlatformMailConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.infra.PlatformMailConfigParam;
import cn.daxpay.open.platform.system.result.config.infra.PlatformMailConfigResult;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台邮件发件箱配置服务
///
/// 管理 SMTP 发件服务器配置，数据通过加密配置服务进行加密存储;
/// 发送器缓存在通知模块内按配置指纹自动失效, 本类无需联动清理
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformMailConfigService {

    private final SystemPlatformEncryptConfigService encryptConfigService;

    /// 获取邮件发件箱配置(不存在时创建默认配置)
    public PlatformMailConfig getMailConfig() {
        return encryptConfigService.getOrCreateConfig(EncryptPlatformConfigTypeEnum.MAIL,
                PlatformMailConfig.class,
                new PlatformMailConfig());
    }

    /// 获取邮件发件箱配置
    public PlatformMailConfigResult findMailConfig() {
        return PlatformMailConfigConvert.CONVERT.toMailResult(this.getMailConfig());
    }

    /// 更新邮件发件箱配置
    /// 授权码为空时保留库中已存值(前端未修改授权码时不传)
    public void updateMailConfig(PlatformMailConfigParam param) {
        PlatformMailConfig data = this.getMailConfig();
        PlatformMailConfigConvert.CONVERT.copy(param, data);
        encryptConfigService.updateConfig(EncryptPlatformConfigTypeEnum.MAIL, data);
    }
}
