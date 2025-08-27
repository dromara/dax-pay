package cn.bootx.platform.common.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * swagger 自动配置
 *
 * @author xxm
 * @since 2020/4/9 13:33
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@RequiredArgsConstructor
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    /**
     * 空白分组(防止knife4j报错)
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx-platform.common.swagger", value = "enabled", havingValue = "true",
            matchIfMissing = true)
    public GroupedOpenApi blankApi() {
        return GroupedOpenApi.builder().group("空白页").packagesToScan("null.null").build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "bootx-platform.common.swagger", value = "enabled", havingValue = "true",
            matchIfMissing = true)
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .contact(new Contact().name(swaggerProperties.getAuthor()))
                        .license(new License().name(swaggerProperties.getLicenseName()).url(swaggerProperties.getLicenseUrl())))
                .externalDocs(new ExternalDocumentation().url(swaggerProperties.getTermsOfServiceUrl()));
    }
}
