package cn.bootx.platform.daxpay.mq;

import cn.bootx.platform.daxpay.code.PaymentEventCode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列配置
 *
 * @author xxm
 * @since 2021/6/25
 */
//@Configuration
public class PaymentRabbitMqConfiguration {

    /** 支付完成队列 */
    @Bean
    public Queue payComplete() {
        return new Queue(PaymentEventCode.PAY_COMPLETE);
    }

    /** 支付关闭/撤销队列 */
    @Bean
    public Queue payCancel() {
        return new Queue(PaymentEventCode.PAY_CANCEL);
    }

    /** 支付退款队列 */
    @Bean
    public Queue payRefund() {
        return new Queue(PaymentEventCode.PAY_REFUND);
    }

    /** 支付超时通知队列 */
    @Bean
    public Queue paymentExpiredTime() {
        return new Queue(PaymentEventCode.PAYMENT_EXPIRED_TIME);
    }

    /** 交换机 */
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PaymentEventCode.EXCHANGE_PAYMENT);
    }

    /** 绑定支付完成 */
    @Bean
    public Binding bindPayComplete() {
        return BindingBuilder.bind(payComplete()).to(paymentExchange()).with(PaymentEventCode.PAY_COMPLETE);
    }

    /** 绑定支付关闭/撤销 */
    @Bean
    public Binding bindPayCancel() {
        return BindingBuilder.bind(payCancel()).to(paymentExchange()).with(PaymentEventCode.PAY_CANCEL);
    }

    /** 绑定支付退款 */
    @Bean
    public Binding bindPayRefund() {
        return BindingBuilder.bind(payRefund()).to(paymentExchange()).with(PaymentEventCode.PAY_REFUND);
    }

    /** 绑定支付超时通知 */
    @Bean
    public Binding bindPaymentExpiredTime() {
        return BindingBuilder.bind(paymentExpiredTime())
            .to(paymentExchange())
            .with(PaymentEventCode.PAYMENT_EXPIRED_TIME);
    }

}
