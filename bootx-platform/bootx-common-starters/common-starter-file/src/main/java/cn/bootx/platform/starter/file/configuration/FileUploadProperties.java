package cn.bootx.platform.starter.file.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置
 *
 * @author xxm
 * @since 2022/1/14
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "bootx.starter.file-upload")
public class FileUploadProperties {

    /**
     * 是否通过后端服务进行文件访问的代理
     * 开启 Web -> 后端服务 -> 具体的的文件服务
     */
    private boolean serviceProxy = true;

    /**
     * 文件服务访问地址
     */
    private String serverUrl = "http://127.0.0.1:9999";

}
