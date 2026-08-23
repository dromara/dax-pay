package cn.daxpay.open.platform.common.swagger;

import cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/// # swagger 自动配置
///
@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(PlatformCommonProperties.class)
@RequiredArgsConstructor
public class SwaggerAutoConfiguration {

    private static final String[] PLATFORM_BASE_PACKAGES = {
            "cn.daxpay.open.platform.common",
            "cn.daxpay.open.platform.capability",
            "cn.daxpay.open.platform.baseapi",
            "cn.daxpay.open.platform.iam",
            "cn.daxpay.open.platform.system",
            "cn.daxpay.open.platform.notice"
    };

    private static final String[] PAYMENT_PACKAGES = {
            "cn.daxpay.open.payment"
    };

    private static final String[] CHANNEL_PACKAGES = {
            "cn.daxpay.open.channel"
    };

    private final PlatformCommonProperties platformCommonProperties;

    @Bean
    public OpenAPI openApi() {
        var swagger = platformCommonProperties.getSwagger();
        var info = new Info()
                .title(defaultValue(swagger.getTitle(), "DaxPay API"))
                .description(defaultValue(swagger.getDescription(), "DaxPay 接口文档"))
                .version(defaultValue(swagger.getVersion(), "4.0.0-beta4"));

        if (StringUtils.hasText(swagger.getAuthor())) {
            info.contact(new Contact().name(swagger.getAuthor()));
        }

        var openApi = new OpenAPI().info(info);
        if (StringUtils.hasText(swagger.getTermsOfServiceUrl())) {
            openApi.externalDocs(new ExternalDocumentation().url(swagger.getTermsOfServiceUrl()));
        }
        return openApi;
    }

    @Bean
    @ConditionalOnClass(GroupedOpenApi.class)
    public GroupedOpenApi platformBaseGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("platform-base")
                .displayName("基础服务（平台基础、IAM、系统管理、通知等）")
                .packagesToScan(PLATFORM_BASE_PACKAGES)
                .build();
    }

    @Bean
    @ConditionalOnClass(GroupedOpenApi.class)
    public GroupedOpenApi paymentGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("payment")
                .displayName("支付平台（交易、订单、退款、商户侧能力）")
                .packagesToScan(PAYMENT_PACKAGES)
                .build();
    }

    @Bean
    @ConditionalOnClass(GroupedOpenApi.class)
    public GroupedOpenApi channelGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("channel")
                .displayName("支付通道（微信、支付宝等渠道配置与回调）")
                .packagesToScan(CHANNEL_PACKAGES)
                .build();
    }

    private String defaultValue(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value : defaultValue;
    }
}

