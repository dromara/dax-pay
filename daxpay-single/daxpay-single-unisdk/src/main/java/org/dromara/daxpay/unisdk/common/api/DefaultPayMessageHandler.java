package org.dromara.daxpay.unisdk.common.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.unisdk.common.bean.PayMessage;
import org.dromara.daxpay.unisdk.common.bean.PayOutMessage;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 默认处理支付回调消息的处理器接口
 *
 * 主要用来处理支付相关的业务
 * @author  egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2018-10-29 17:31:05
 * </pre>
 */
@Slf4j
public class DefaultPayMessageHandler implements PayMessageHandler<PayMessage, PayService> {

    protected final Logger LOG = LoggerFactory.getLogger(DefaultPayMessageHandler.class);
    /**
     * @param payMessage 支付消息
     * @param context    上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService 支付服务
     * @return xml, text格式的消息，如果在异步规则里处理的话，可以返回null
     */
    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        if (log.isInfoEnabled()) {
            log.info("回调支付消息处理器，回调消息：" + JSON.toJSONString(payMessage));
        }
        return payService.successPayOutMessage(payMessage);
    }
}
