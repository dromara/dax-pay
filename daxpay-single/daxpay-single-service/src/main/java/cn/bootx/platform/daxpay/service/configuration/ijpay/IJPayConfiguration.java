package cn.bootx.platform.daxpay.service.configuration.ijpay;

import com.ijpay.core.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * IJPau配置类
 * @author xxm
 * @since 2024/2/26
 */
@Slf4j
@Configuration
public class IJPayConfiguration {

    /**
     * 更改为自己的HttpDelegate, 自动获取系统中支持的TLS版本
     */
    @PostConstruct
    public void initHttpDelegate() throws NoSuchAlgorithmException {
        log.info("初始化IJPay配置类");
        String tlsVersion = SSLContext.getDefault()
                .getSupportedSSLParameters()
                .getProtocols()[0];
        HttpKit.setDelegate(new DaxPayHttpDelegate(tlsVersion));
    }

}
