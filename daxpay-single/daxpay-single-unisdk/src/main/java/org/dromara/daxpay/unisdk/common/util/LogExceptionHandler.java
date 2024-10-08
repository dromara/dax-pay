package org.dromara.daxpay.unisdk.common.util;

import org.dromara.daxpay.unisdk.common.api.PayErrorExceptionHandler;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * LogExceptionHandler 日志处理器
 * @author  egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016-6-1 11:28:01
 *
 *
 * source chanjarster/weixin-java-tools
 * </pre>
 */
public class LogExceptionHandler implements PayErrorExceptionHandler {

    protected final Logger LOGGER = LoggerFactory.getLogger(PayErrorExceptionHandler.class);

    @Override
    public void handle(PayErrorException e) {

        LOGGER.error("Error happens", e);

    }

}
