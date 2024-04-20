package cn.daxpay.multi.admin;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

/**
 * 商户运营端
 * @author xxm
 * @since 2024/4/20
 */
@Slf4j
@SpringBootApplication
public class DaxpayAdminStart {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DaxpayAdminStart.class, args);
        Environment env = application.getEnvironment();
        // 环境变量
        String appName = env.getProperty("spring.application.name");

        // 应用信息栏
        String appInfo = StrUtil.format("应用 '{}' 运行成功! \n\t", appName);

        log.info("\n----------------------------------------------------------\n\t{}", appInfo);
    }
}
