package cn.daxpay.open.platform.capability.file.provider;

import cn.daxpay.open.platform.capability.file.entity.FileStorageConfig;

import java.util.Optional;

/// # 平台OSS配置提供者接口
///
/// 由 service-system 模块实现并注入，capability-file 通过此接口获取平台OSS配置
public interface OssConfigProvider {

    /// 获取默认的OSS配置
    /// @return 文件存储配置
    Optional<FileStorageConfig> getDefaultConfig();
}

