package cn.bootx.platform.daxpay.service.configuration.ijpay;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.net.SSLProtocols;
import com.ijpay.core.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

        String[] protocols = SSLContext.getDefault()
                .getSupportedSSLParameters()
                .getProtocols();
        List<String> list = ListUtil.toList(protocols);
        // 是否支持TLS1.2
        if (list.contains(SSLProtocols.TLSv12)) {
            HttpKit.setDelegate(new DaxPayHttpDelegate(SSLProtocols.TLSv12));
            return;
        }
        // 是否支持TLS1.1
        if (list.contains(SSLProtocols.TLSv11)) {
            HttpKit.setDelegate(new DaxPayHttpDelegate(SSLProtocols.TLSv11));
            return;
        }
        // 如果都不支持, 使用TLS_V1
        HttpKit.setDelegate(new DaxPayHttpDelegate(SSLProtocols.TLSv1));
    }

}
