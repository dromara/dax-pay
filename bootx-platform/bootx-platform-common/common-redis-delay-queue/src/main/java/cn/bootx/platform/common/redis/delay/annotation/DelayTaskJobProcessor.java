package cn.bootx.platform.common.redis.delay.annotation;

import cn.bootx.platform.common.redis.delay.bean.Job;
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
 * 延时任务接收方法注册和调用方法
 * @author xxm
 * @since 2024/7/31
 */
@Configuration
public class DelayTaskJobProcessor implements BeanPostProcessor {

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
            // 找到被注解修饰的方法
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
    public void run(String topic, Job<?> o) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Method> entry : annotatedMethods.entrySet()) {
            String beanMethodName = entry.getKey();
            String beanName = beanMethodName.split("#")[0];
            Method method = entry.getValue();
            Object proxyBean = proxyBeans.get(beanName);
            DelayTaskJob annotation = method.getAnnotation(DelayTaskJob.class);
            if (Objects.equals(topic, annotation.value()))
                // 满足结果只有一个参数且参数类型为DelayTaskResult
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(DelayTaskEvent.class)) {
                    var result = new DelayTaskEvent<>()
                            .setId(o.getId())
                            .setTopic(o.getTopic())
                            .setDelayTime(o.getDelayTime())
                            .setMessage(o.getMessage())
                            .setStatus(o.getStatus())
                            .setTtrTime(o.getTtrTime());

                    method.invoke(proxyBean, result);
                }
        }
    }
}
