package org.dromara.daxpay.payment.common.context;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// # 支付作用域注册配置
///
@Configuration
public class PaymentScopeConfig implements BeanFactoryPostProcessor {

    @Bean
    public PaymentScope paymentScope() {
        return new PaymentScope();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.registerScope(PaymentScope.SCOPE_NAME, beanFactory.getBean(PaymentScope.class));
    }
}
