package cn.daxpay.open.platform.system.result.config.platform;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台OSS配置
///
@Data
@Accessors(chain = true)
@Schema(title = "平台OSS配置")
public class PlatformOssConfigResult {

    /// 服务端点
    @Schema(description = "服务端点")
    private String endpoint;

    /// 存储区域
    @Schema(description = "存储区域")
    private String region;

    /// 公开存储桶
    @Schema(description = "公开存储桶")
    private String publicBucket;

    /// 私有存储桶
    @Schema(description = "私有存储桶")
    private String privateBucket;

    /// 公开访问域名
    @Schema(description = "公开访问域名")
    private String publicBaseUrl;

    /// 私有访问域名
    @Schema(description = "私有访问域名")
    private String privateBaseUrl;

    /// 访问密钥（AccessKey），通常由云服务商提供
    @SensitiveInfo
    @Schema(description = "访问密钥（AccessKey），通常由云服务商提供")
    private String accessKey;

    /// 私有密钥（SecretKey），请妥善保管
    @SensitiveInfo
    @Schema(description = "私有密钥（SecretKey），请妥善保管")
    private String secretKey;

    /// 路径样式访问
    @Schema(description = "路径样式访问")
    private Boolean pathStyleAccess;

    /// 上传预签名URL有效期（分钟）
    @Schema(description = "上传预签名URL有效期（分钟）")
    private Integer uploadExpireMinutes;

    /// 下载或查看预签名URL有效期（小时）
    @Schema(description = "下载或查看预签名URL有效期（小时）")
    private Integer downloadExpireHours;

    /// 基础存储路径
    @Schema(description = "基础存储路径")
    private String basePath;
}
