package org.dromara.x.file.storage.core.platform;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dromara.x.file.storage.core.FileStorageProperties.AmazonS3Config;

import java.util.Map;

/**
 * 重写 Amazon S3 存储平台的 Client 工厂, 支持 withPathStyleAccessEnabled 配置
 */
@Getter
@Setter
@NoArgsConstructor
public class AmazonS3FileStorageClientFactory implements FileStorageClientFactory<AmazonS3> {
    private String platform;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endPoint;
    private Map<String, Object> attr;
    private volatile AmazonS3 client;

    public AmazonS3FileStorageClientFactory(AmazonS3Config config) {
        platform = config.getPlatform();
        accessKey = config.getAccessKey();
        secretKey = config.getSecretKey();
        region = config.getRegion();
        endPoint = config.getEndPoint();
        attr = config.getAttr();
    }

    @Override
    public AmazonS3 getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                            .withCredentials(
                                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
                    if (StrUtil.isNotBlank(endPoint)) {
                        builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region));
                    } else if (StrUtil.isNotBlank(region)) {
                        builder.withRegion(region);
                    }
                    // 使用路径访问样式
                    Boolean forcePathStyle = MapUtil.getBool(attr, "forcePathStyle", false);
                    builder.withPathStyleAccessEnabled(forcePathStyle);
                    client = builder.build();
                }
            }
        }
        return client;
    }

    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }
}
