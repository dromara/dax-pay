package cn.bootx.platform.common.config;

import cn.bootx.platform.common.config.enums.DeployMode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置配置
 *
 * @author xxm
 * @since 2020/4/9 13:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx-platform.config")
public class BootxConfigProperties {

    /** 终端编码, 开启分模块部署后有效 */
    private String clientCode = "";

    /**
     * 终端列表, 开启融合模式后才会生效, 表示当前系统集成了哪些终端模块
     */
    private List<String> clientCodes = new ArrayList<>();

    /** 数据加密配置 */
    private Encrypt encrypt = new Encrypt();

    /** 系统部署方式 */
    private DeployMode deployMode = DeployMode.FUSION;

    /**
     * 数据加密配置
     */
    @Data
    @Accessors(chain = true)
    public static class Encrypt{
        /** 是否开启加密 */
        private boolean enable = false;

        /** 数据加密密钥 AES密钥格式, 需要为 32 字节（256位） */
        private String encryptKey;
    }

}
