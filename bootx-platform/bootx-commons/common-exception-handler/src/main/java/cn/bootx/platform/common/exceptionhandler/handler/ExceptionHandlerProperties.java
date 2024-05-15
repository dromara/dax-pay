package cn.bootx.platform.common.exceptionhandler.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置
 *
 * @author xxm
 * @since 2021/12/5
 */
@Getter
@Setter
@ConfigurationProperties("bootx.common.exception")
public class ExceptionHandlerProperties {

    /** 是否显示详细异常信息 */
    private boolean showFullMessage;

}
