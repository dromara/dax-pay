package cn.bootx.platform.starter.file.configuration;

import cn.hutool.core.util.StrUtil;
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
@ConfigurationProperties(prefix = "bootx-platform.starter.file-upload")
public class FileUploadProperties {

    /**
     * 文件访问转发地址(当前后端服务地址或被代理后的地址), 流量会经过后端服务的转发
     */
    private String forwardServerUrl = "http://127.0.0.1:9999";

    /**
     * 处理为 / 结尾
     */
    public String getForwardServerUrl() {
        return StrUtil.removeSuffix(forwardServerUrl, "/");
    }
}
