package org.dromara.daxpay.platform.capability.file.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 文件存储配置
///
/// 用于传递存储配置信息，不对应数据库表
@Data
@Accessors(chain = true)
public class FileStorageConfig {

    /// 服务端点
    private String endpoint;

    /// 存储区域
    private String region;

    /// 公开存储桶
    private String publicBucket;

    /// 私有存储桶
    private String privateBucket;

    /// 公开访问域名
    private String publicBaseUrl;

    /// 私有访问域名
    private String privateBaseUrl;

    /// 访问密钥
    private String accessKey;

    /// 私有密钥
    private String secretKey;

    /// 路径样式访问
    private boolean pathStyleAccess;

    /// 上传预签名URL有效期（分钟）
    private Integer uploadExpireMinutes;

    /// 下载或查看预签名URL有效期（小时）
    private Integer downloadExpireHours;

    /// 基础存储路径
    private String basePath;

}
