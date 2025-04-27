package cn.bootx.platform.starter.cache.handler;

import jakarta.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 延时任务接收方法注册和调用方法
 * @author xxm
 * @since 2024/7/31
 */
@Configuration
public class CacheClearProcessor implements BeanPostProcessor {

    /**
     * 代理对象列表
     */
    private final Set<String> cachePrefix = new HashSet<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nullable String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            // 找到被注解修饰的方法
            if (method.isAnnotationPresent(Cacheable.class)) {
                Cacheable annotation = method.getAnnotation(Cacheable.class);
                cachePrefix.addAll(Arrays.stream(annotation.value()).toList());
            }
        }
        return bean;
    }

    /**
     * 获取缓存前缀列表
     */
    public List<String> getCachePrefix() {
        return cachePrefix.stream().toList();
    }

}
