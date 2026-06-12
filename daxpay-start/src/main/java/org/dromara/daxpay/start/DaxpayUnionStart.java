package org.dromara.daxpay.start;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

/// # 开源版
@Slf4j
@SpringBootApplication()
public class DaxpayOpenStart {

    static void main(String[] args) throws UnknownHostException {
        var application = SpringApplication.run(DaxpayOpenStart.class, args);
        var env = application.getEnvironment();
        // 环境变量
        var appName = env.getProperty("spring.application.name");
        var host = InetAddress.getLocalHost().getHostAddress();
        var port = env.getProperty("server.port");
        var contextPath = env.getProperty("server.servlet.context-path", "");
        var path = env.getProperty("spring.mvc.servlet.path", "");
        var swaggerUiPath = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui/index.html");

        // 应用信息栏
        var appInfo = StrUtil.format("应用 '{}' 运行成功! ", appName);
        var swagger = StrUtil.format("Swagger UI(IP): \t\thttp://{}:{}{}{}{}", host, port, contextPath, path, swaggerUiPath);
        var localSwagger = StrUtil.format("Swagger UI(本机): \t\thttp://{}:{}{}{}{}", "127.0.0.1", port, contextPath, path, swaggerUiPath);
        String message = System.lineSeparator() +
                "----------------------------------------------------------" + System.lineSeparator() +
                "    " + appInfo + System.lineSeparator() +
                "    " + swagger + System.lineSeparator() +
                "    " + localSwagger + System.lineSeparator() +
                "----------------------------------------------------------";
        log.info(message);
    }
}

