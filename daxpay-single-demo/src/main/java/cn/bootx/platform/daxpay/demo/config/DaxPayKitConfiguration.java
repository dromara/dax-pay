package cn.bootx.platform.daxpay.demo.config;

import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 初始化
 * @author xxm
 * @since 2024/2/8
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DaxPayDemoProperties.class)
public class DaxPayKitConfiguration {

    private final DaxPayDemoProperties daxPayDemoProperties;

    /**
     * 初始化
     */
    @EventListener(ApplicationStartedEvent.class)
//    @EventListener(webstarteve.class)
    public void initDaxPayKit(){
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl(daxPayDemoProperties.getServerUrl())
                .signType(daxPayDemoProperties.getSignType())
                .signSecret(daxPayDemoProperties.getSignSecret())
                .reqTimeout(daxPayDemoProperties.getReqTimeout())
                .build();
        DaxPayKit.initConfig(config);
    }
}
