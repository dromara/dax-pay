package org.dromara.daxpay;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务端
 * @author xxm
 * @since 2024/4/20
 */
@Slf4j
@SpringBootApplication
public class DaxpayServer {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DaxpayServer.class, args);
        Environment env = application.getEnvironment();
        // 环境变量
        String appName = env.getProperty("spring.application.name");
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String path = env.getProperty("spring.mvc.servlet.path", "");

        // 应用信息栏
        String appInfo = StrUtil.format("应用 '{}' 运行成功! ", appName);
        // swagger栏
        String swagger = StrUtil.format("Swagger文档: \t\thttp://{}:{}{}{}/doc.html", host, port, contextPath, path);
        String localSwagger = StrUtil.format("Swagger文档: \t\thttp://{}:{}{}{}/doc.html", "127.0.0.1", port, contextPath, path);

        log.info("""

                ----------------------------------------------------------
                    {}
                    {}
                    {}
                ----------------------------------------------------------""", appInfo, swagger, localSwagger);
    }
}
