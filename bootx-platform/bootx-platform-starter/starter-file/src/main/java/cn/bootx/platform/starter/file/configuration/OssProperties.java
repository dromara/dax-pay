package cn.bootx.platform.starter.file.configuration;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "bootx-platform.starter.oss")
/***
 * oss配置
 */
public class OssProperties {

    private  String filePath;


}
