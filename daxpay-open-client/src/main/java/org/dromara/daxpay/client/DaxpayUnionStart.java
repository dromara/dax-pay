package org.dromara.daxpay.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 运营端
 * @author xxm
 * @since 2024/4/20
 */
@Slf4j
@SpringBootApplication()
public class DaxpayUnionStart {

    public static void main(String[] args) throws UnknownHostException {
        var application = SpringApplication.run(DaxpayUnionStart.class, args);
        var env = application.getEnvironment();
        // 环境变量
        var appName = env.getProperty("spring.application.name");
        var host = InetAddress.getLocalHost().getHostAddress();
        var port = env.getProperty("server.port");
        var contextPath = env.getProperty("server.servlet.context-path", "");
        var path = env.getProperty("spring.mvc.servlet.path", "");

        // 应用信息栏
        var appInfo = StrUtil.format("应用 '{}' 运行成功! ", appName);
        // swagger栏
        var swagger = StrUtil.format("Swagger文档: \t\thttp://{}:{}{}{}/doc.html", host, port, contextPath, path);
        var localSwagger = StrUtil.format("Swagger文档: \t\thttp://{}:{}{}{}/doc.html", "127.0.0.1", port, contextPath, path);
        log.info("""

                ----------------------------------------------------------
                    {}
                    {}
                    {}
                ----------------------------------------------------------""", appInfo, swagger, localSwagger);
    }
}
