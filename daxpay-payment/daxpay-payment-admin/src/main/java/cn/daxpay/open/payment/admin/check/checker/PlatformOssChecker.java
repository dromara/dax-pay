package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformOssConfig;
import cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.service.config.SystemPlatformEncryptConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/// # 对象存储配置检查器
///
/// 检测 OSS 关键字段是否已配置。OSS 未配置时文件上传/Logo 存储等能力不可用。
/// 配置记录可能被前端页签的懒创建落为空记录(存在性判断会被空记录击穿), 故须按字段判空而非只判记录存在。
@Component
@Order(4)
@RequiredArgsConstructor
public class PlatformOssChecker implements AdminConfigChecker {

    private final SystemPlatformEncryptConfigService systemPlatformEncryptConfigService;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_OSS;
    }

    @Override
    public ConfigCheckItem check() {
        // 只读获取(无记录返回 null), 不触发懒创建写库
        PlatformOssConfig config = systemPlatformEncryptConfigService.getConfig(
                EncryptPlatformConfigTypeEnum.OSS, PlatformOssConfig.class);
        // 服务端点/密钥/桶任一为空 => 告警
        boolean unconfigured = Objects.isNull(config)
                || StrUtil.isBlank(config.getEndpoint())
                || StrUtil.isBlank(config.getAccessKey())
                || StrUtil.isBlank(config.getSecretKey())
                || StrUtil.isBlank(config.getPublicBucket())
                || StrUtil.isBlank(config.getPrivateBucket());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PLATFORM_OSS,
                    ConfigCheckCategoryEnum.PLATFORM_OSS.getCode(),
                    "configCheck.platformOss.title",
                    "configCheck.platformOss.description",
                    "/system/config/platform",
                    Map.of("tab", "oss")
            );
        }
        return null;
    }
}
