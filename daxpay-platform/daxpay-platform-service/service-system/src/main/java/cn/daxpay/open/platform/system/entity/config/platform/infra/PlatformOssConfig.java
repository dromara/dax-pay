package cn.daxpay.open.platform.system.entity.config.platform.infra;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/// # 对象存储配置
///
@Data
@Accessors(chain = true)
public class PlatformOssConfig {

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
    private Boolean pathStyleAccess;

    /// 上传预签名URL有效期（分钟）
    private Integer uploadExpireMinutes;

    /// 下载或查看预签名URL有效期（小时）
    private Integer downloadExpireHours;

    /// 基础存储路径
    private String basePath;

    public Boolean getPathStyleAccess() {
        return Objects.equals(pathStyleAccess, Boolean.TRUE);
    }
}
