package cn.bootx.platform.common.swagger;

import cn.hutool.core.util.ArrayUtil;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * swagger 自动配置
 * @author xxm
 * @since 2025/2/6
 */
@Slf4j
@Component
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerPostConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private final String swaggerPropertiesPrefix = "bootx-platform.common.swagger";

    private SwaggerProperties swaggerProperties;

    /**
     * 手动注册swagger docket bean, 如果晚于swagger加载后, 再注入的bean将不会生效
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry registry) throws BeansException {
        var basePackages = this.swaggerProperties.getBasePackages();
        AtomicInteger atomicInteger = new AtomicInteger(96);
        basePackages.forEach((name, basePackage) -> {
            var packages = ArrayUtil.toArray(basePackage, String.class);
            var bean = new RootBeanDefinition(GroupedOpenApi.class, () -> GroupedOpenApi.builder().group(name).packagesToScan(packages).build());
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
