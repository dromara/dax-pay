package cn.bootx.daxpay;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * DaxPay 支付开发平台
 * @author xxm
 * @date 2023/4/20
 */
@Slf4j
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
@SpringBootApplication
public class DaxPayApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DaxPayApplication.class, args);
        Environment env = application.getEnvironment();
        // 环境变量
        String appName = env.getProperty("spring.application.name");
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String path = env.getProperty("spring.mvc.servlet.path", "");

        // 应用信息栏
        String appInfo = StrUtil.format("应用 '{}' 运行成功! \n\t", appName);
        // swagger栏
        String swagger = StrUtil.format("Swagger文档: \t\thttp://{}:{}{}{}/doc.html\n\t", host, port, contextPath, path);

        log.info("\n----------------------------------------------------------\n\t" + "{}{} \n"
                + "----------------------------------------------------------", appInfo, swagger);
    }

}

