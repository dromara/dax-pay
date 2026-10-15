package cn.daxpay.open.platform.system.param.config.infra;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 平台OSS配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "平台OSS配置参数")
public class PlatformOssConfigParam {

    /// 服务端点
    @NotBlank(message = "{validation.field.endpoint.notBlank}")
    @Schema(description = "服务端点")
    private String endpoint;

    /// 存储区域
    @Schema(description = "存储区域")
    private String region;

    /// 公开存储桶
    @NotBlank(message = "{validation.field.publicBucket.notBlank}")
    @Schema(description = "公开存储桶")
    private String publicBucket;

    /// 私有存储桶
    @NotBlank(message = "{validation.field.privateBucket.notBlank}")
    @Schema(description = "私有存储桶")
    private String privateBucket;

    /// 公开访问域名
    ///
    /// 必须包含协议前缀(http:// 或 https://): 无协议的地址经 302 跳转时会被客户端当作相对路径解析, 导致公开文件无法访问。
    /// 允许留空(公开桶不用直链的场景)。
    @Pattern(regexp = "^$|^https?://.+$", message = "{validation.field.publicBaseUrl.pattern}")
    @Schema(description = "公开访问域名, 须包含协议前缀(http:// 或 https://)")
    private String publicBaseUrl;

    /// 公开访问URL拼接方式: bucketLevel-桶级域名(缺省) / withBucket-带桶名
    @Schema(description = "公开访问URL拼接方式: bucketLevel-桶级域名(缺省) / withBucket-带桶名")
    private String publicUrlMode;

    /// 访问密钥（AccessKey），通常由云服务商提供
    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "访问密钥（AccessKey），通常由云服务商提供")
    private String accessKey;

    /// 私有密钥（SecretKey），请妥善保管
    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
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
