package cn.bootx.platform.common.redis.delay.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 延时任务
 * @author xxm
 * @since 2024/7/31
 */
@Configuration
public class DelayTaskJobBeanPostProcessor implements BeanPostProcessor {

    /**
     * 代理对象列表
     */
    private final Map<String, Object> proxyBeans = new HashMap<>();
    /**
     * 代理方法列表
     */
    private final Map<String, Method> annotatedMethods = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nullable String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            // 创建代理对象并添加到map列表中
            if (method.isAnnotationPresent(DelayTaskJob.class)) {
                annotatedMethods.put(beanName + "#" + method.getName(), method);
                if (!proxyBeans.containsKey(beanName)) {
                    proxyBeans.put(beanName, bean);
                }
                break;
            }
        }
        return bean;
    }

    /**
     * 执行任务
     */
    public void run(String topic, Object o) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Method> entry : annotatedMethods.entrySet()) {
            String beanMethodName = entry.getKey();
            String beanName = beanMethodName.split("#")[0];
            Method method = entry.getValue();
            Object proxyBean = proxyBeans.get(beanName);
            DelayTaskJob annotation = method.getAnnotation(DelayTaskJob.class);
            if (Objects.equals(topic, annotation.value()))
                // 根据参数类型动态调用方法
                if (method.getParameterCount() == 1) {
                    method.invoke(proxyBean, o);
                }
        }
    }
}
