package cn.daxpay.single.demo.configuration;

import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
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
                .serviceUrl(daxPayDemoProperties.getServerUrl())   // 服务地址
                .signType(daxPayDemoProperties.getSignType())      // 签名类型
                .signSecret(daxPayDemoProperties.getSignSecret())  // 签名密钥
                .reqTimeout(daxPayDemoProperties.getReqTimeout())  // 超时时间
                .build();
        DaxPayKit.initConfig(config);
    }
}
