package cn.daxpay.open.platform.capability.file.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 公开访问URL拼接方式枚举
///
@Getter
@RequiredArgsConstructor
public enum PublicUrlModeEnum {

    /// 桶级域名: URL = 域名/对象Key, 适用于 R2/阿里云OSS/腾讯云COS 等桶级自定义域名(缺省)
    BUCKET_LEVEL("bucketLevel", "桶级域名"),
    /// 带桶名: URL = 域名/桶名/对象Key, 适用于 MinIO 等多桶共根的根地址
    WITH_BUCKET("withBucket", "带桶名");

    private final String code;

    /// 中文名称(展示用)
    private final String name;
}
