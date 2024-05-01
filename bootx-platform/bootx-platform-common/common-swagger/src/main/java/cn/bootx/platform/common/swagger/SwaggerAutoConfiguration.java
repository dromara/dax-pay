package cn.bootx.platform.common.swagger;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.atomic.AtomicInteger;

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
public class SwaggerAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private final String swaggerPropertiesPrefix = "bootx-platform.common.swagger";

    private SwaggerProperties swaggerProperties;

    /**
     * 创建swagger文档模块
     * @param name 模块名称
     * @param basePackage 扫描路径数组
     */
    private GroupedOpenApi createApi(String name, String... basePackage) {
        return GroupedOpenApi.builder().group(name).packagesToScan(basePackage).build();
    }

    /**
     * 空白分组(防止knife4j报错)
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx-platform.common.swagger", value = "enabled", havingValue = "true",
            matchIfMissing = true)
    public GroupedOpenApi blankApi() {
        return this.createApi(" 空白页", "null.null");
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

    /**
     * 手动注册swagger docket bean, 如果晚于swagger加载后, 再注入的bean将不会生效
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry registry) throws BeansException {
        var basePackages = this.swaggerProperties.getBasePackages();
        AtomicInteger atomicInteger = new AtomicInteger(96);
        basePackages.forEach((name, basePackage) -> {
            var packages = ArrayUtil.toArray(basePackage, String.class);
            var bean = new RootBeanDefinition(GroupedOpenApi.class, () -> this.createApi(name, packages));
            registry.registerBeanDefinition((char) atomicInteger.incrementAndGet() + "ModelAPi", bean);
        });
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 获取环境变量并设置到swaggerProperties对象中, bean注册时, 参数还未进行绑定
     */
    @Override
    public void setEnvironment(@NotNull Environment environment) {
        BindResult<SwaggerProperties> bind = Binder.get(environment)
                .bind(swaggerPropertiesPrefix, SwaggerProperties.class);
        this.swaggerProperties = bind.orElse(new SwaggerProperties());
    }

}
