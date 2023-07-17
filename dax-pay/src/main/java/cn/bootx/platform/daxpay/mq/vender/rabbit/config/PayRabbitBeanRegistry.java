package cn.bootx.platform.daxpay.mq.vender.rabbit.config;

import cn.bootx.platform.daxpay.code.PaymentEventCode;
import cn.bootx.platform.daxpay.mq.event.PayEvent;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.amqp.core.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.bootx.platform.daxpay.code.PaymentEventCode.DELAYED_EXCHANGE_PAYMENT;

/**
 * 注入Rabbit相关的消息类
 * @author xxm
 * @since 2023/7/17
 */
@Configuration
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "rabbit")
public class PayRabbitBeanRegistry implements BeanDefinitionRegistryPostProcessor {


    /** 自定义交换机： 用于延迟消息 **/
    @Bean
    CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_PAYMENT, "x-delayed-message", true, false, args);
    }

    /**
     * 注册Bean
     * @param beanDefinitionRegistry 应用程序上下文使用的 Bean 定义注册表
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        this.initRabbitBeans(beanDefinitionRegistry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /**
     * 初始化RabbitMQ相关的消息, 并注册到Bean容器中
     */
    public void initRabbitBeans(BeanDefinitionRegistry beanDefinitionRegistry){
        // 扫描所有的消息类
        Set<Class<?>> set = ClassUtil.scanPackageBySuper(ClassUtil.getPackage(PayEvent.class), PayEvent.class);
        // 遍历
        for (Class<?> clazz : set) {
            // 实例化
            PayEvent eventMsg = (PayEvent) ReflectUtil.newInstance(clazz);

            // 注册队列(Queue)
            RootBeanDefinition queueDefinition = new RootBeanDefinition(Queue.class, () -> new Queue(eventMsg.getQueueName()));
            beanDefinitionRegistry.registerBeanDefinition(eventMsg.getQueueName(),queueDefinition);

            // 注册与交换机的绑定关系Binding
            Queue queue = SpringUtil.getBean(eventMsg.getQueueName(), Queue.class);
            RootBeanDefinition bindingDefinition = new RootBeanDefinition(Binding.class,
                    BindingBuilder
                            .bind(queue)
                            .to(delayedExchange())
                            .with(PaymentEventCode.PAY_REFUND)::noargs);
            beanDefinitionRegistry.registerBeanDefinition(eventMsg.getQueueName()+".exchange",bindingDefinition);
        }
    }
}
