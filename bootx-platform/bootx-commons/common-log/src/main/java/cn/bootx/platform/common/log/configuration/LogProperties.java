package cn.bootx.platform.common.log.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置
 * @author xxm
 * @since 2023/3/29
 */
@Getter
@Setter
@ConfigurationProperties("bootx.common.log")
public class LogProperties {

    /** PlumeLog 相关配置 */
    private PlumeLog plumeLog = new PlumeLog();

    /**
     * PlumeLog 相关配置
     */
    @Getter
    @Setter
    public static class PlumeLog{
        /** 超时天数  */
        private int keepDays = 30;
    }
}
