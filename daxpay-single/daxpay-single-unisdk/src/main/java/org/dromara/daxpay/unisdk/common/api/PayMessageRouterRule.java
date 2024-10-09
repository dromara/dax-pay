package org.dromara.daxpay.unisdk.common.api;


import lombok.Getter;
import lombok.Setter;
import org.dromara.daxpay.unisdk.common.bean.PayMessage;
import org.dromara.daxpay.unisdk.common.bean.PayOutMessage;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;

import java.util.*;
import java.util.regex.Pattern;


/**
 * Route规则 路由
 *
 * @author egan
 * <pre>
 *  email egzosn@gmail.com
 *  date 2016-6-1 11:28:01
 *
 *
 *  source chanjarster/weixin-java-tools
 *  </pre>
 */
@Getter
public class PayMessageRouterRule {


    private final PayMessageRouter routerBuilder;
    /**
     * 默认同步
     */
    @Setter
    private boolean async = false;

    /**
     * 支付类型
     */
    @Setter
    private String payType;
    /**
     * 交易类型
     */
    @Setter
    private String[] transactionType;
    /**
     * 简介
     */
    @Setter
    private String subject;
    /**
     * 正则匹配
     */
    @Setter
    private String rSubject;
    /**
     * 匹配的键名称
     */
    @Setter
    private String key;
    /**
     * 匹配的键名称对应的值 正则
     */
    @Setter
    private String rValue;

    @Setter
    private boolean reEnter = false;

    @Setter
    private List<PayMessageHandler> handlers = new ArrayList<>();

    @Setter
    private List<PayMessageInterceptor> interceptors = new ArrayList<>();

    public PayMessageRouterRule(PayMessageRouter routerBuilder) {
        this.routerBuilder = routerBuilder;
    }

    /**
     * 设置是否异步执行，默认是true
     *
     * @param async 是否异步执行，默认是true
     * @return Route规则
     */
    public PayMessageRouterRule async(boolean async) {
        this.async = async;
        return this;
    }


    /**
     * 如果payType等于某值
     *
     * @param payType 支付类型
     * @return Route规则
     */
    public PayMessageRouterRule payType(String payType) {
        this.payType = payType;
        return this;
    }

    /**
     * 如果transactionType等于某值
     *
     * @param transactionType 交易类型
     * @return Route规则
     */
    public PayMessageRouterRule transactionType(String... transactionType) {
        this.transactionType = transactionType;
        return this;
    }


    /**
     * 如果subject等于某值
     *
     * @param subject 简介
     * @return Route规则
     */
    public PayMessageRouterRule subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * 如果subject匹配该正则表达式
     *
     * @param regex 简介正则
     * @return Route规则
     */
    public PayMessageRouterRule rSubject(String regex) {
        this.rSubject = regex;
        return this;
    }

    /**
     * 如果subject匹配该正则表达式
     *
     * @param key   需要匹配支付消息内键的名字
     * @param regex key值对应的正则
     * @return Route规则
     */
    public PayMessageRouterRule key2RValue(String key, String regex) {
        this.key = key;
        this.rValue = regex;
        return this;
    }


    /**
     * 设置消息拦截器
     *
     * @param interceptor 消息拦截器
     * @return Route规则
     */
    public PayMessageRouterRule interceptor(PayMessageInterceptor interceptor) {
        return interceptor(interceptor, (PayMessageInterceptor[]) null);
    }

    /**
     * 设置消息拦截器
     *
     * @param interceptor       消息拦截器
     * @param otherInterceptors 其他消息拦截器
     * @return Route规则
     */
    public PayMessageRouterRule interceptor(PayMessageInterceptor interceptor, PayMessageInterceptor... otherInterceptors) {
        this.interceptors.add(interceptor);
        if (otherInterceptors != null && otherInterceptors.length > 0) {
            this.interceptors.addAll(Arrays.asList(otherInterceptors));
        }
        return this;
    }

    /**
     * 设置消息处理器
     *
     * @param handler 消息处理器
     * @return Route规则
     */
    public PayMessageRouterRule handler(PayMessageHandler handler) {
        return handler(handler, (PayMessageHandler[]) null);
    }

    /**
     * 设置消息处理器
     *
     * @param handler       消息处理器
     * @param otherHandlers 其他消息处理器
     * @return Route规则
     */
    public PayMessageRouterRule handler(PayMessageHandler handler, PayMessageHandler... otherHandlers) {
        this.handlers.add(handler);
        if (otherHandlers != null && otherHandlers.length > 0) {
            this.handlers.addAll(Arrays.asList(otherHandlers));
        }
        return this;
    }

    /**
     * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
     *
     * @return Route规则
     */
    public PayMessageRouter end() {
        this.routerBuilder.getRules().add(this);
        return this.routerBuilder;
    }

    /**
     * 规则结束，但是消息还会进入其他规则
     *
     * @return Route规则
     */
    public PayMessageRouter next() {
        this.reEnter = true;
        return end();
    }

    /**
     * 将支付事件修正为不区分大小写,
     * 比如框架定义的事件常量为
     *
     * @param payMessage 支付消息
     * @return 是否匹配通过
     */
    protected boolean test(PayMessage payMessage) {
        return (
                (this.payType == null || this.payType.equals((payMessage.getPayType() == null ? null : payMessage.getPayType())))
                        &&
                        (this.transactionType == null || equalsTransactionType(payMessage.getTransactionType()))
                        &&
                        (this.key == null || this.rValue == null || Pattern
                                .matches(this.rValue, payMessage.getPayMessage().get(key) == null ? "" : payMessage.getPayMessage().get(key).toString().trim()))
                        &&
                        (this.subject == null || this.subject
                                .equals(payMessage.getSubject() == null ? null : payMessage.getSubject().trim()))
                        &&
                        (this.rSubject == null || Pattern
                                .matches(this.rSubject, payMessage.getSubject() == null ? "" : payMessage.getSubject().trim()))
        )
                ;
    }

    /**
     * 匹配交易类型
     *
     * @param transactionType 交易类型
     * @return 匹配交易类型
     */
    public boolean equalsTransactionType(String transactionType) {
        if (null == transactionType) {
            return false;
        }

        for (String type : this.getTransactionType()) {
            if (type.equalsIgnoreCase((transactionType))) {
                return true;
            }
        }
        return false;

    }


    /**
     * 返回支付响应消息
     *
     * @param payMessage       支付消息
     * @param payService       支付服务
     * @param exceptionHandler 异常处理器
     * @return 支付响应消息
     */
    protected PayOutMessage service(PayMessage payMessage,
                                    UniPayService payService,
                                    PayErrorExceptionHandler exceptionHandler) {

        try {

            Map<String, Object> context = new HashMap<>();
            // 如果拦截器不通过
            for (PayMessageInterceptor interceptor : this.interceptors) {
                if (!interceptor.intercept(payMessage, context, payService)) {
                    //这里直接返回成功，解决外层判断问题
                    return payService.successPayOutMessage(payMessage);
                }
            }

            // 交给handler处理
            PayOutMessage res = null;
            for (PayMessageHandler handler : this.handlers) {
                // 返回最后handler的结果
                res = handler.handle(payMessage, context, payService);
            }
            return res;
        }
        catch (PayErrorException e) {
            exceptionHandler.handle(e);
        }
        return null;

    }

}
