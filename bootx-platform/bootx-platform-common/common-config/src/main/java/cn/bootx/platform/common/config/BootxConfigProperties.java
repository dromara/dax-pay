package cn.bootx.platform.common.config;

import cn.bootx.platform.common.config.enums.DeployMode;
import lombok.Getter;
import lombok.Setter;
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

    /** 终端编码 */
    @Deprecated
    private String clientCode = "";

    /**
     * 终端列表, 开启融合模式后才会生效, 表示当前系统集成了哪些终端模块
     */
    private List<String> clientCodes = new ArrayList<>();

    /** 系统部署方式 */
    private DeployMode deployMode = DeployMode.FUSION;

}
